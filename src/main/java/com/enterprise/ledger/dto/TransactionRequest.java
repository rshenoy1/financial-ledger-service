package com.enterprise.ledger.dto;

import java.math.BigDecimal;

/**
 * Hardened production-grade DTO mapping incoming JSON transactional payloads.
 * Satisfies JavaBean contract conventions for smooth Jackson serialization.
 */
public class TransactionRequest {
    
    private BigDecimal amount;

    // Explicit Default Constructor required for JSON serialization reflection
    public TransactionRequest() {}

    public TransactionRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() { 
        return amount; 
    }
    
    public void setAmount(BigDecimal amount) { 
        this.amount = amount; 
    }
}