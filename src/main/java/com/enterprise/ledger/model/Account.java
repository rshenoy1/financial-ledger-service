package com.enterprise.ledger.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Enterprise core Account database entity model.
 * Hardened for massive concurrency using non-blocking Optimistic Locking mechanics.
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_holder", nullable = false, length = 100)
    private String accountHolder;

    @Column(name = "balance", nullable = false, precision = 18, scale = 4)
    private BigDecimal balance;

    /**
     * 🛡️ Optimistic Locking Version Hook.
     * Intercepts simultaneous out-of-order write attempts across Virtual Threads,
     * throwing an ObjectOptimisticLockingFailureException instead of letting race 
     * conditions corrupt ledger balances.
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    // Default Constructor required by JPA
    public Account() {}

    // Production Construction Paradigm
    public Account(String accountHolder, BigDecimal balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    // Standard Encapsulated Accessor Infrastructure
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Long getVersion() { return version; }
    protected void setVersion(Long version) { this.version = version; }
}
