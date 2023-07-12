package com.bak.bankaccountkata.service;

import com.bak.bankaccountkata.domain.BankAccount;
import com.bak.bankaccountkata.domain.Operation;
import com.bak.bankaccountkata.domain.OperationType;
import com.bak.bankaccountkata.interfaces.IOperations;
import com.bak.bankaccountkata.repository.BankAccountRepository;
import com.bak.bankaccountkata.repository.OperationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class OperationService implements IOperations {
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;

    public OperationService(OperationRepository operationRepository, BankAccountRepository bankAccountRepository) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public BankAccount withdrawal(Long bankaccountId, BigDecimal amount) throws IllegalAccessException {
        Operation operation = performOperation(bankaccountId, amount, OperationType.WITHDRAWAL);
        Optional<BankAccount> oBankAccount = bankAccountRepository.findById(bankaccountId);
        if (!oBankAccount.isPresent()) throw new IllegalStateException("wrong bankAccountId : " + bankaccountId);
        BankAccount bankAccount = oBankAccount.get();
        bankAccount.getOperations().add(operation);
        return bankAccount;
    }

    @Override
    public BankAccount deposit(Long bankaccountId, BigDecimal amount) throws IllegalAccessException {
        Operation operation = performOperation(bankaccountId, amount, OperationType.DEPOSIT);
        Optional<BankAccount> oBankAccount = bankAccountRepository.findById(bankaccountId);
        if (!oBankAccount.isPresent()) throw new IllegalStateException("wrong bankAccountId : " + bankaccountId);
        BankAccount bankAccount = oBankAccount.get();
        bankAccount.getOperations().add(operation);
        return bankAccount;
    }

    @Override
    public Operation performOperation(Long bankaccountId, BigDecimal amount, OperationType operationType) throws IllegalAccessException {
        BankAccount account = bankAccountRepository.findById(bankaccountId).orElseThrow(() -> new IllegalAccessException(": " + bankaccountId));

        BigDecimal opType = operationType.equals(OperationType.WITHDRAWAL) ? BigDecimal.valueOf(-1) : BigDecimal.ONE;
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setDate(Instant.now());
        operation.setBankaccount(account);
        operation.setType(operationType);
        BigDecimal newbalance = account.getBalance().add(amount.multiply(opType));
        if (newbalance.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalStateException("not enough balance: " + bankaccountId);
        account.setBalance(newbalance);
        operationRepository.save(operation);
        return operation;
    }

    /*
        history (operation, date, amount, balance) of my operations
    */
    @Override
    public void printStatement(Long bankaccountId) throws IllegalAccessException {
        List<Object[]> listBankAccount = operationRepository.operationHistory(bankaccountId);
        if (listBankAccount == null) {
            throw new IllegalAccessException("list not found : " + bankaccountId);
        }
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).withZone(ZoneId.from(ZoneOffset.UTC));
        ;

        System.out.println("\nOperation  |        DATE          |    AMOUNT    |  BALANCE");
        listBankAccount.stream().forEach(obj -> {
            String date = formmat1.format(Instant.parse((String) obj[1]));
            System.out.println(
                    (String) obj[0] + "      " + date + "       " +
                            (new BigDecimal((String) obj[2]).setScale(2, RoundingMode.HALF_EVEN).toPlainString() + "       " +
                                    (new BigDecimal((String) obj[3]).setScale(2, RoundingMode.HALF_EVEN).toPlainString())));
        });

    }
}
