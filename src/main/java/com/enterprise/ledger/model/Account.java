package com.enterprise.ledger.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Enterprise core Account database entity model.
 * Hardened for massive concurrency using explicit database sequence parameters.
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq_gen")
    @SequenceGenerator(name = "accounts_seq_gen", sequenceName = "accounts_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "account_holder", nullable = false, length = 100)
    private String accountHolder;

    @Column(name = "balance", nullable = false, precision = 18, scale = 4)
    private BigDecimal balance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public Account() {}

    public Account(String accountHolder, BigDecimal balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public Long getVersion() { return version; }
    protected void setVersion(Long version) { this.version = version; }
}