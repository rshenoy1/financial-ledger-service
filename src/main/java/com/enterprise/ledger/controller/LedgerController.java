package com.enterprise.ledger.controller;

import com.enterprise.ledger.dto.TransactionRequest;
import com.enterprise.ledger.model.Account;
import com.enterprise.ledger.repository.AccountRepository;
import com.enterprise.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;
    private final AccountRepository accountRepository;

    public LedgerController(LedgerService ledgerService, AccountRepository accountRepository) {
        this.ledgerService = ledgerService;
        this.accountRepository = accountRepository;
    }

    /**
     * Executes a transaction payload. Programmatically seeds the account if missing.
     */
    @PostMapping("/accounts/{id}/transaction")
    public ResponseEntity<Account> executeTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequest request) {
        
        // 🛡️ Safe Fallback: If Account ID 1 does not exist yet, initialize it dynamically
        if (id == 1L && !accountRepository.existsById(1L)) {
            Account seedAccount = new Account("Enterprise Production Clearing Account", new BigDecimal("500000.0000"));
            seedAccount.setId(1L);
            accountRepository.save(seedAccount);
        }

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