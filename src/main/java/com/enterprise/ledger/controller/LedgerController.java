package com.enterprise.ledger.controller;

import com.enterprise.ledger.dto.TransactionRequest;
import com.enterprise.ledger.dto.TransactionResponse;
import com.enterprise.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

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
        
        // 📊 Telemetry Probe: Simulate network lag to allow Prometheus scrapers to capture thread density
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        TransactionResponse response = ledgerService.executeTransaction(id, request.amount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/thread-status")
    public ResponseEntity<Map<String, Object>> getThreadStatus() {
        return ResponseEntity.ok(Map.of(
            "threadName", Thread.currentThread().toString(),
            "isVirtualThread", Thread.currentThread().isVirtual()
        ));
    }
}