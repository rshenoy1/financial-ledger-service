package com.enterprise.ledger.dto;

import java.time.LocalDateTime;

/**
 * Standardized corporate error response structure.
 * guarantees consistent JSON envelopes for downstream client consumption.
 */
public record ErrorResponse(
    String message,
    String details,
    LocalDateTime timestamp
) {}