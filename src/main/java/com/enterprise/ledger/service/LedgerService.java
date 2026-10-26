package com.enterprise.ledger.service;

import com.enterprise.ledger.model.Account;
import com.enterprise.ledger.repository.AccountRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Enterprise Service Layer coordinating financial ledger transactions.
 * Leverages non-blocking optimistic retry loops to handle high concurrent workloads smoothly.
 */
@Service
public class LedgerService {

    private final AccountRepository accountRepository;

    public LedgerService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Executes a high-concurrency balance modification transaction with automated optimistic retry logic.
     * Prevents database lock contention while safely mutating financial balances.
     */
    public Account adjustBalanceWithRetry(Long accountId, BigDecimal amountDelta) {
        int maxAttempts = 3;
        int backoffMs = 50;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                // Delegate to an internal transactional block to guarantee proper commitment boundaries
                return executeAdjustmentTransaction(accountId, amountDelta);
            } catch (ObjectOptimisticLockingFailureException ex) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Transaction failed: High concurrency conflict ceiling reached after " + maxAttempts + " attempts.", ex);
                }
                try {
                    // Non-blocking wait state allowing concurrent virtual worker contexts to clear down rows
                    Thread.sleep(backoffMs * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Transaction interrupted during concurrent retry delay.", ie);
                }
            }
        }
        throw new RuntimeException("Unexpected transaction termination state.");
    }

    @Transactional
    protected Account executeAdjustmentTransaction(Long accountId, BigDecimal amountDelta) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Target account ID " + accountId + " not found."));

        BigDecimal updatedBalance = account.getBalance().add(amountDelta);
        if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction aborted: Insufficient ledger funds available.");
        }

        account.setBalance(updatedBalance);
        return accountRepository.save(account);
    }
}
