package com.bak.bankaccountkata.service;

import com.bak.bankaccountkata.domain.BankAccount;
import com.bak.bankaccountkata.domain.OperationType;
import com.bak.bankaccountkata.interfaces.IBankAccounts;
import com.bak.bankaccountkata.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class BankAccountService implements IBankAccounts {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void printBalance(OperationType type, Long bankAccountId) throws IllegalAccessException {

        Optional<BankAccount> oBankAccount = bankAccountRepository.findById(bankAccountId);
        if (!oBankAccount.isPresent()) throw new IllegalStateException("wrong bankAccountId : " + bankAccountId);
        BankAccount ba = oBankAccount.get();
        System.out.println(" Operation :" + type + "   Account Id :" + ba.getId() + "  Balance :" + ba.getBalance().toPlainString());
    }

    @Override
    public BankAccount createAccount(BigDecimal balance) throws IllegalAccessException {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalStateException("wrong balance : " + balance);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(balance);
        return bankAccountRepository.save(bankAccount);
    }
}
