package com.learning.atm_project.accounts;

import com.learning.atm_project.AccountType;
import com.learning.atm_project.exceptions.InsufficientFunds;

class BankAccountClient implements BankAccount {
    private final AccountType accountType = AccountType.CLIENT;
    private int id;
    private String owner;
    private String key;
    private int balance;

    BankAccountClient(int id, String owner, String key) {
        this(id, owner, key, 0);
    }

    BankAccountClient(int id, String owner, String key, int balance) {
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

    private synchronized void processDeposit(int money) {
        this.balance += money;
    }

    private synchronized void processWithdraw(int money) {
        int newBalance = balance - money;
        if (newBalance < 0) throw new InsufficientFunds();
        balance = newBalance;
    }

    public void deposit(int money) {
        processDeposit(money);
    }

    public void withdraw(int money) {
        processWithdraw(money);
    }

    public int getBalance() {
        return this.balance;
    }
}

