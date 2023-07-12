package com.bak.bankaccountkata.interfaces;

import com.bak.bankaccountkata.domain.BankAccount;
import com.bak.bankaccountkata.domain.Operation;
import com.bak.bankaccountkata.domain.OperationType;

import java.math.BigDecimal;

public interface IOperations {
    BankAccount withdrawal(Long bankaccountId, BigDecimal amount) throws IllegalAccessException;

    BankAccount deposit(Long bankaccountId, BigDecimal amount) throws IllegalAccessException;

    Operation performOperation(Long bankaccountId, BigDecimal amount, OperationType operationType) throws IllegalAccessException;

    void printStatement(Long bankaccountId) throws IllegalAccessException;

}
