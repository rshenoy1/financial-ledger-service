package com.enterprise.ledger.controller;

import com.enterprise.ledger.dto.TransactionRequest;
import com.enterprise.ledger.dto.TransactionMutationResponse;
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
    public ResponseEntity<TransactionMutationResponse> processTransaction(
            @PathVariable Long id, 
            @RequestBody TransactionRequest request) {
        
        // 📊 Telemetry Probe: Induce 200ms non-blocking parking to ensure sampling registration
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Hardened Fix: Route connection parameters straight to executeMutation
        TransactionMutationResponse response = ledgerService.executeMutation(id, request.amount());
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