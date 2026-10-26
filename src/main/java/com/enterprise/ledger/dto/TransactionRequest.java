package com.enterprise.ledger.dto;

import java.math.BigDecimal;

/**
 * Hardened production-grade DTO mapping incoming JSON transactional payloads.
 * Explicitly satisfies full Jackson deserializer JavaBean access requirements.
 */
public class TransactionRequest {
    
    private BigDecimal amount;

    // Mandatory No-Args Constructor required for dynamic JSON Reflection
    public TransactionRequest() {}

    public TransactionRequest(BigDecimal amount) {
        this.amount = amount;
    }

    // Public Getters and Setters satisfying JavaBean specifications
    public BigDecimal getAmount() { 
        return amount; 
    }
    
    public void setAmount(BigDecimal amount) { 
        this.amount = amount; 
    }
}