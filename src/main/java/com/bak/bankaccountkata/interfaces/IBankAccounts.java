package com.bak.bankaccountkata.interfaces;

import com.bak.bankaccountkata.domain.BankAccount;
import com.bak.bankaccountkata.domain.OperationType;

import java.math.BigDecimal;

public interface IBankAccounts {
    void printBalance(OperationType type, Long bankAccountId) throws IllegalAccessException;

    BankAccount createAccount(BigDecimal balance) throws IllegalAccessException;


}
