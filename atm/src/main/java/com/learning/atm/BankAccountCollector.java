package com.learning.atm;

import java.math.BigDecimal;

public class BankAccountCollector implements BankAccount {
    private final AccountType accountType = AccountType.COLLECTOR;
    private int id;
    private String owner;

    BankAccountCollector(int id, String owner) {
        this.id = id;
        this.owner = owner;
    }

    @Override
    public int getAccountId() {
        return this.id;
    }

    @Override
    public AccountType getAccountType() {
        return this.accountType;
    }

    @Override
    public String getOwner() {
        return this.owner;
    }

    @Override
    public void deposit(BigDecimal deposit) {
    }

    @Override
    public void withdraw(BigDecimal money) {
    }

    @Override
    public BigDecimal getBalance() {
        return null;
    }
}
