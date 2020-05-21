package com.learning.atm_project.accounts;

import com.learning.atm_project.AccountType;

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
}
