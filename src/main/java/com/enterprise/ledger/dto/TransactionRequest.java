package com.enterprise.ledger.dto;

import java.math.BigDecimal;

public class TransactionRequest {
    private BigDecimal amount;

    public TransactionRequest() {}
    public TransactionRequest(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}