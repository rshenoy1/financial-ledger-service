package com.enterprise.ledger.controller;

import com.enterprise.ledger.dto.TransactionRequest;
import com.enterprise.ledger.dto.TransactionResponse;
import com.enterprise.ledger.model.Account;
import com.enterprise.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping("/accounts/{id}/transaction")
    public ResponseEntity<TransactionResponse> processTransaction(
            @PathVariable Long id, 
            @RequestBody TransactionRequest request) {
        
        // Execute the balanced mutations using your precise retry method signature
        Account updatedAccount = ledgerService.adjustBalanceWithRetry(id, request.amount());
        
        // Map the database entity outputs onto your fresh TransactionResponse contract
        TransactionResponse response = new TransactionResponse(
            System.currentTimeMillis(),
            updatedAccount.getId(),
            request.amount(),
            request.amount().compareTo(java.math.BigDecimal.ZERO) >= 0 ? "CREDIT" : "DEBIT",
            Instant.now(),
            updatedAccount.getBalance()
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stress-threads")
    public ResponseEntity<Map<String, Object>> stressThreads() {
        int targetThreads = 50;
        ExecutorService executor;

        try {
            java.lang.reflect.Method method = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            executor = (ExecutorService) method.invoke(null);
        } catch (Exception e) {
            executor = Executors.newCachedThreadPool();
        }

        try {
            for (int i = 0; i < targetThreads; i++) {
                executor.submit(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(12);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        } finally {
            executor.shutdown();
        }

        return ResponseEntity.ok(Map.of(
            "status", "Sustained load active",
            "activeGaugesAllocated", targetThreads,
            "durationSeconds", 12
        ));
    }

    @GetMapping("/thread-status")
    public ResponseEntity<Map<String, Object>> getThreadStatus() {
        return ResponseEntity.ok(Map.of(
            "threadName", Thread.currentThread().toString(),
            "isVirtualThread", Thread.currentThread().isVirtual()
        ));
    }
}