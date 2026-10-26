package com.enterprise.ledger.dto;

import java.math.BigDecimal;

/**
 * Modern production-grade immutable request DTO using Java 21 Record syntax.
 * Natively supports Jackson deserialization without boilerplate accessors or constructors.
 */
public record TransactionRequest(BigDecimal amount) {
    // Canonical constructors, getters, and standard mappings are automatically handled by the JVM
}