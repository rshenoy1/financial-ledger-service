package com.enterprise.ledger.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
    Long id,
    Long accountId,
    BigDecimal amount,
    String type,
    Instant timestamp,
    BigDecimal resultingBalance
) {}