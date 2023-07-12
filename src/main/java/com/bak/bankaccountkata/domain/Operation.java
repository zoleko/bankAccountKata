package com.bak.bankaccountkata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "operation")
public class Operation {

    @SequenceGenerator(name = "ID", sequenceName = "ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID")
    @Id
    private Long id;

    private Instant date = Instant.now();

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount bankaccount;


    public Operation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public BankAccount getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(BankAccount bankaccount) {
        this.bankaccount = bankaccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
