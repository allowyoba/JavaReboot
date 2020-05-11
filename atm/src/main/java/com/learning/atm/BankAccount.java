package com.learning.atm;

import java.math.BigDecimal;

interface BankAccount {
    int getAccountId();
    AccountType getAccountType();
    String getOwner();

    void deposit(BigDecimal money);
    void withdraw(BigDecimal money) throws InsufficientFunds;
    BigDecimal getBalance();
}
