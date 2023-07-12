package com.bak.bankaccountkata;

import com.bak.bankaccountkata.domain.BankAccount;
import com.bak.bankaccountkata.domain.Operation;
import com.bak.bankaccountkata.domain.OperationType;
import com.bak.bankaccountkata.repository.BankAccountRepository;
import com.bak.bankaccountkata.repository.OperationRepository;
import com.bak.bankaccountkata.service.BankAccountService;
import com.bak.bankaccountkata.service.OperationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankaccountkataApplicationTests {

    @InjectMocks
    private BankAccountService bankAccountService;

    @InjectMocks
    private OperationService operationService;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private OperationRepository operationRepository;

    private static BankAccount bankAccount;

    @Test
    void contextLoads() {
    }


    @BeforeAll
    static void initAccount() {
        //given
        bankAccount = new BankAccount(1L, new BigDecimal(200));


    }

    @Test
    void make_a_deposit() throws IllegalAccessException {
        //Given bankAccount
        //when
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);
        when(bankAccountRepository.findById(eq(1L))).thenReturn(java.util.Optional.of(bankAccount));

        BankAccount actual = bankAccountRepository.save(bankAccount);

        //Then
        assertThat(actual).usingRecursiveComparison().isEqualTo(bankAccount);
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
        verifyNoMoreInteractions(bankAccountRepository);

        //Given
        Operation operation = new Operation();
        operation.setAmount(new BigDecimal(500));
        operation.setDate(Instant.now());
        operation.setBankaccount(bankAccount);
        operation.setType(OperationType.DEPOSIT);
        //When
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);

        operationService.deposit(1L, operation.getAmount());
        bankAccountService.printBalance(operation.getType(), 1L);
        //Then
        assertThat(bankAccount.getBalance()).isEqualTo(new BigDecimal(700));
    }

    @Test
    void make_a_withdrawal() throws IllegalAccessException {

        BigDecimal oldbalance = bankAccount.getBalance();
        when(bankAccountRepository.findById(eq(1L))).thenReturn(java.util.Optional.of(bankAccount));
        //Given
        Operation operation = new Operation();
        operation.setAmount(new BigDecimal(100));
        operation.setDate(Instant.now());
        operation.setBankaccount(bankAccount);
        operation.setType(OperationType.WITHDRAWAL);
        //When
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);

        operationService.withdrawal(1L, operation.getAmount());
        bankAccountService.printBalance(operation.getType(), 1L);
        //Then
        assertThat(bankAccount.getBalance()).isEqualTo(oldbalance.subtract(new BigDecimal(100)));
    }

    @Test
    void make_a_withdrawal_notEnough_Balance() throws IllegalAccessException {


        //Given
        Operation operation = new Operation();
        operation.setAmount(new BigDecimal(1000));
        operation.setDate(Instant.now());
        operation.setBankaccount(bankAccount);
        operation.setType(OperationType.WITHDRAWAL);
        //When
        when(bankAccountRepository.findById(eq(1L))).thenReturn(java.util.Optional.of(bankAccount));

        //Then
        assertThatThrownBy(() -> operationService.withdrawal(1L, operation.getAmount()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not enough balance");
        bankAccountService.printBalance(operation.getType(), 1L);

    }

    @Test
    void check_history() throws IllegalAccessException {
        //When
        List<Object[]> listBankAccount = new ArrayList<>();
        for (Operation op : bankAccount.getOperations()) {
            Object[] obj = new Object[]{
                    op.getType().name(), op.getDate().toString(), op.getAmount().toPlainString(), op.getBankaccount().getBalance().toPlainString()};
            listBankAccount.add(obj);
        }
        //When
        when(operationRepository.operationHistory(any(Long.class))).thenReturn(listBankAccount);
        operationService.printStatement(1L);
    }

    @Test
    void flow() throws IllegalAccessException {
        make_a_deposit();
        make_a_withdrawal();
        make_a_withdrawal_notEnough_Balance();
        check_history();
    }
}
