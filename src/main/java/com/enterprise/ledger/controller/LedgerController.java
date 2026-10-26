package com.enterprise.ledger.controller;

import com.enterprise.ledger.dto.TransactionRequest;
import com.enterprise.ledger.dto.TransactionMutationResponse;
import com.enterprise.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping("/accounts/{id}/transaction")
    public ResponseEntity<TransactionMutationResponse> processTransaction(
            @PathVariable Long id, 
            @RequestBody TransactionRequest request) {
        TransactionMutationResponse response = ledgerService.executeMutation(id, request.amount());
        return ResponseEntity.ok(response);
    }

    // ⚡ TELEMETRY DRILL: Spawns and locks 500 Virtual Threads for 10 seconds to force a visual telemetry spike
    @GetMapping("/stress-threads")
    public ResponseEntity<Map<String, Object>> stressThreads() {
        int threadCount = 500;
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    latch.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        return ResponseEntity.ok(Map.of(
            "message", "Successfully spawned 500 virtual worker threads locked for 10 seconds!",
            "targetCount", threadCount
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