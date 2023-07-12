package com.bak.bankaccountkata.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "account")
public class BankAccount {

    @SequenceGenerator(name = "ID", sequenceName = "ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID")
    @Id
    private Long id;

    private BigDecimal balance;

    @OneToMany(mappedBy = "bankaccount")
    private List<Operation> operations = new ArrayList<>();


    public BankAccount() {
    }

    public BankAccount(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
