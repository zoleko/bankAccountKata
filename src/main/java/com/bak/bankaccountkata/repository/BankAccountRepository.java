package com.bak.bankaccountkata.repository;


import com.bak.bankaccountkata.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {


}