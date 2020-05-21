package com.learning.atm_project.accounts;

import com.learning.atm_project.AccountType;

interface BankAccount {
    int getAccountId();
    AccountType getAccountType();
    String getOwner();
}
