package com.enterprise.ledger.controller;

import com.enterprise.ledger.model.Account;
import com.enterprise.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Enterprise Ingress API Controller for ledger actions.
 * Incoming network request payloads map directly to non-blocking Virtual Threads.
 */
@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    /**
     * Executes a high-concurrency transaction update against a target ledger account.
     */
    @PostMapping("/accounts/{id}/transaction")
    public ResponseEntity<Account> executeTransaction(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        
        Account updatedAccount = ledgerService.adjustBalanceWithRetry(id, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * Diagnostic hook allowing engineers to verify the active runtime execution environment context.
     * Demonstrates whether the thread handling the web payload is a Virtual Thread.
     */
    @GetMapping("/thread-status")
    public ResponseEntity<Map<String, Object>> getThreadStatus() {
        Thread currentThread = Thread.currentThread();
        
        Map<String, Object> statusMetrics = Map.of(
                "threadName", currentThread.getName(),
                "isVirtualThread", currentThread.isVirtual(), // Returns true under Project Loom configurations
                "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(statusMetrics);
    }
}
