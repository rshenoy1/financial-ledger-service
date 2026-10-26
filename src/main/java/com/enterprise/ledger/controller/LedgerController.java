package com.enterprise.ledger.controller;

import com.enterprise.ledger.dto.TransactionRequest;
import com.enterprise.ledger.model.Account;
import com.enterprise.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping("/accounts/{id}/transaction")
    public ResponseEntity<Account> executeTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequest request) {
        Account updatedAccount = ledgerService.adjustBalanceWithRetry(id, request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/thread-status")
    public ResponseEntity<Map<String, Object>> getThreadStatus() {
        Thread currentThread = Thread.currentThread();
        Map<String, Object> statusMetrics = Map.of(
                "threadName", currentThread.getName(),
                "isVirtualThread", currentThread.isVirtual(),
                "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.ok(statusMetrics);
    }
}