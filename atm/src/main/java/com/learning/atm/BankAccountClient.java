package com.learning.atm;

import java.math.BigDecimal;

class BankAccountClient implements BankAccount {
    private final AccountType accountType = AccountType.CLIENT;
    private int id;
    private String owner;
    private String key;
    private BigDecimal balance;

    BankAccountClient(int id, String owner, String key) {
        this(id, owner, key, BigDecimal.ZERO);
    }

    BankAccountClient(int id, String owner, String key, BigDecimal balance) {
        this.id = id;
        this.owner = owner;
        this.key = key;
        this.balance = balance;
    }

    @Override
    public int getAccountId() {
        return this.id;
    }

    @Override
    public AccountType getAccountType() {
        return this.accountType;
    }

    public String getOwner() {
        return this.owner;
    }

    private synchronized void processDeposit(BigDecimal money) {
        BigDecimal newBalance = balance.add(money);
        System.out.println("Средства в размере " + money + " успешно добавлены!\nСумма на счету " + newBalance);
        balance = newBalance;
    }

    private synchronized void processWithdraw(BigDecimal money) throws InsufficientFunds {
        BigDecimal newBalance = balance.subtract(money);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFunds();
        }
    }

    @Override
    public void deposit(BigDecimal money) {
        processDeposit(money);
    }

    @Override
    public void withdraw(BigDecimal money) throws InsufficientFunds {
        processWithdraw(money);
    }

    @Override
    public BigDecimal getBalance() {
        return this.balance;
    }
}

class InsufficientFunds extends Exception {
}