package com.bak.bankaccountkata.repository;


import com.bak.bankaccountkata.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {


    @Query("" +
            "SELECT op.type , op.date, op.amount,ba.balance " +
            "FROM Operation op inner join BankAccount ba " +
            "ON op.bankaccount.id = ba.id " +
            "WHERE ba.id=:id " +
            "ORDER BY op.date desc"
    )
    List<Object[]> operationHistory(@Param("id") long id);
}