package com.learning.atm;

import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

class ATM {
    private final int[] FACE_VALUES = { 100, 200, 500, 1000, 5000 };
    private Cell[] cells = null;
    private int minimumRemainingFaceValue;
    private BigDecimal currentBalance = null;

    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:mem:BankAccount;INIT=RUNSCRIPT FROM 'classpath:DBInit.sql'";
    private final String DB_USER = "BankManager";
    private final String DB_PASSWORD = "VeryStrongPassword";

    private final String QUERY_GET_CLIENT_ACCOUNT = "" +
            "select a.account_id, a.owner, a.balance " +
            "from bank.account a, bank.card c " +
            "where c.card_number = '%s' and c.card_key = '%s' and a.account_id = c.account_id;";

    private final String QUERY_GET_COLLECTOR_ACCOUNT = "" +
            "select account_id, owner " +
            "from bank.account " +
            "where account_id = '%s' and account_password = '%s' and account_type_id = (" +
            "   select account_type_id" +
            "   from bank.accounttype" +
            "   where account_type = 'COLLECTOR'" +
            ");";

    private Connection connection = null;
    private Statement statement = null;

    private BankAccount bankAccount = null;

    private ATM() throws SQLException, ClassNotFoundException, CrowdedCell {
        this(null);
    }

    private ATM(Cell[] cells) throws SQLException, ClassNotFoundException, CrowdedCell {
        this.currentBalance = BigDecimal.ZERO;
        if (cells == null) {
            this.cells = new Cell[FACE_VALUES.length];
            for (int i = 0; i < FACE_VALUES.length; i++) {
                this.cells[i] = new Cell();
            }
        } else {
            this.cells = cells;
            for (int i = 0; i < FACE_VALUES.length; i++) {
                int cellSize = this.cells[i].getCurrentSize();
                this.currentBalance.add(BigDecimal.valueOf(cellSize * FACE_VALUES[i]));
            }
        }

        getMinimumRemainingFaceValue();

        try {
            Class.forName(JDBC_DRIVER);

            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            this.statement = this.connection.createStatement();
        } catch (Exception se) {
            se.printStackTrace();
            try {
                if (statement != null) statement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException se3) {
                se3.printStackTrace();
            }
        }
    }


    private void print(String text) {
        print(text, 2);
    }

    private void print(String text, int nextLine) {
        System.out.println(text);
        for (int i = 0; i < nextLine; i++)
            System.out.println();
    }

    private BankAccountClient getClientAccountData(String cardNumber, String cardKey) throws SQLException {
        ResultSet resultSet = this.statement.executeQuery(String.format(this.QUERY_GET_CLIENT_ACCOUNT, cardNumber, cardKey));
        if (resultSet.next()) {
            int account_id = resultSet.getInt("ACCOUNT_ID");
            String owner = resultSet.getString("OWNER");
            BigDecimal balance = new BigDecimal(resultSet.getString("BALANCE"));
            BankAccountClient bankAccount = new BankAccountClient(account_id, owner, cardKey, balance);
            return bankAccount;
        }
        return null;
    }

    private BankAccountCollector getCollectorAccountData(String id, String password) throws SQLException {
        ResultSet resultSet = this.statement.executeQuery(String.format(this.QUERY_GET_COLLECTOR_ACCOUNT, id, password));
        if (resultSet.next()) {
            int account_id = resultSet.getInt("ACCOUNT_ID");
            String owner = resultSet.getString("OWNER");
            BankAccountCollector bankAccount = new BankAccountCollector(account_id, owner);
            return bankAccount;
        }
        return null;
    }

    private void createSessionClient(String cardNumber, String cardKey) throws SQLException, UserNotFound {
        this.bankAccount = getClientAccountData(cardNumber, cardKey);
        if (this.bankAccount == null) throw new UserNotFound();
        print("Добро пожаловать, " + this.bankAccount.getOwner());
    }

    private void createSessionCollector(String account_id, String password) throws SQLException, UserNotFound {
        this.bankAccount = getCollectorAccountData(account_id, password);
        if (this.bankAccount == null) throw new UserNotFound();
        print("Добро пожаловать, " + this.bankAccount.getOwner());
    }

    public void insertCard(String cardNumber, String cardKey) throws SQLException, UserNotFound {
        try {
            createSessionClient(cardNumber, cardKey);
        } catch (UserNotFound ex) {
            print("Неверные данные");
            returnCard();
        }
    }

    public void login(String account_id, String password) {
        try {
            createSessionCollector(account_id, password);
        } catch (UserNotFound | SQLException ex) {
            print("Неверные данные");
        }
    }

    private void returnCard() {
        bankAccount = null;
        print("Заберите карту");
    }

    private void getMinimumRemainingFaceValue() {
        int i = 0;
        while (i < this.FACE_VALUES.length && this.cells[i].getCurrentSize() == 0) {
            i++;
        }
        if (i == FACE_VALUES.length) this.minimumRemainingFaceValue = 0;
        else this.minimumRemainingFaceValue = FACE_VALUES[i];
    }

    private Cell[] getCells() throws InsufficientPrivileges {
        if ((bankAccount == null) || (bankAccount.getAccountType().equals(AccountType.CLIENT)))
            throw new InsufficientPrivileges();
        return this.cells;
    }

    private int[] getBills(BigDecimal sum) throws NotEnoughBills, InsufficientPrivileges {
        if ((bankAccount == null) || (bankAccount.getAccountType().equals(AccountType.COLLECTOR)))
            throw new InsufficientPrivileges();

        BigDecimal sumToGet = sum;
        if (sum.compareTo(currentBalance) > 0 || (minimumRemainingFaceValue == 0) ||
                (sum.remainder(BigDecimal.valueOf(minimumRemainingFaceValue)).compareTo(BigDecimal.ZERO) > 0))
            throw new NotEnoughBills();
        int[] bills = new int[] {0, 0, 0, 0, 0, 0};
        for (int i = FACE_VALUES.length - 1; i >= 0; i--) {
            if (sum.compareTo(BigDecimal.valueOf(FACE_VALUES[i])) < 0) continue;
            else {
                bills[i] = Math.min(sum.divide(BigDecimal.valueOf(FACE_VALUES[i]), 0, RoundingMode.CEILING).intValue(), this.cells[i].getCurrentSize());
                this.cells[i].getBills(bills[i]);
                sum = sum.subtract(BigDecimal.valueOf(bills[i] * FACE_VALUES[i]));
                if (sum.equals(BigDecimal.ZERO)) break;
            }
        }
        getMinimumRemainingFaceValue();
        currentBalance = currentBalance.subtract(sumToGet);
        return bills;
    }

    private int[] putBills(int[] bills) throws CrowdedCell {
        if (bankAccount == null) {
            return bills;
        }

        for (int i = 0; i < FACE_VALUES.length; i++) {
            int currentCellBillCount = this.cells[i].getCurrentSize();
            if (bills[i] + currentCellBillCount > Cell.getMaximumSize()) return bills;
        }

        BigDecimal deposit = BigDecimal.ZERO;

        for (int i = 0; i < FACE_VALUES.length; i++) {
            this.cells[i].putBills(bills[i]);
            deposit = deposit.add(BigDecimal.valueOf(bills[i] * FACE_VALUES[i]));

        }
        this.currentBalance = this.currentBalance.add(deposit);
        getMinimumRemainingFaceValue();
        print("Успешный депозит " + deposit.toString());

        if (bankAccount.getAccountType().equals(AccountType.CLIENT))
            bankAccount.deposit(deposit);

        return null;
    }

    public int[] withdraw(BigDecimal sum) throws NotEnoughBills, InsufficientPrivileges {
        try {
            return getBills(sum);
        } catch (NotEnoughBills ex) {
            print("Невозможно выдать запрошенную сумму");
        } catch (InsufficientPrivileges ex) {
            print("Недостаточно привилегий");
        }
        return null;
    }

    public int[] deposit(int[] bills) throws CrowdedCell {
        try {
            return putBills(bills);
        } catch (CrowdedCell ex) {
            print("Невозможно внести деньги");
            return bills;
        }
    }

    public void printCellsCount() {
        try {
            Cell[] cells = getCells();
            for (int i = 0; i < FACE_VALUES.length; i++) {
                print("Ячейка " + i + ", номинал " + FACE_VALUES[i] + ": " + cells[i].getCurrentSize(), 0);
            }
            print("");
        } catch (InsufficientPrivileges ex) {
            print("Недостаточно привилегий");
        }
    }

    private BigDecimal getBalance() throws InsufficientPrivileges {
        if ((bankAccount == null) || (bankAccount.getAccountType().equals(AccountType.COLLECTOR)))
            throw new InsufficientPrivileges();
        return bankAccount.getBalance();
    }

    public BigDecimal checkBalance() throws InsufficientPrivileges {
        try {
            return getBalance();
        } catch (InsufficientPrivileges ex) {
            print("Недостаточно привилегий");
            return null;
        }
    }

    private BigDecimal getCurrentBalance() throws InsufficientPrivileges {
        if ((bankAccount == null) || (bankAccount.getAccountType().equals(AccountType.CLIENT)))
            throw new InsufficientPrivileges();
        return currentBalance;
    }

    public String toString() {
        String result = new String(currentBalance + "\n");
        for (int i = 0; i < FACE_VALUES.length; i++) {
            result += FACE_VALUES[i] + ": " + cells[i].getCurrentSize() + "\n";
        }
        return result;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, CrowdedCell, NotEnoughBills, InsufficientPrivileges, UserNotFound {
        ATM atm = new ATM();
        atm.login("4", "StrongPassword4");
        atm.printCellsCount();
        atm.deposit(new int[] {10, 12, 13, 4, 50});
        atm.printCellsCount();
        atm.insertCard("1234123412340011", "0001");
        atm.deposit(new int[] {2, 3, 4, 5, 6});
        System.out.println(atm);
        int[] bills = atm.withdraw(BigDecimal.valueOf(240510));
//        for (int i = 0; i < bills.length; i++) {
//            System.out.println(bills[i]);
//        }
        System.out.println(atm);
//        int[] a = atm.getBills(BigDecimal.valueOf(10));
//        if (a == null) System.out.println("Запрошенная сумма не может быть выдана");
    }
}

class InsufficientPrivileges extends Exception {}
class UserNotFound extends Exception {}
