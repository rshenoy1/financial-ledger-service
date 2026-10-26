package com.enterprise.ledger.config;

import com.enterprise.ledger.model.Account;
import com.enterprise.ledger.repository.AccountRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * Hardened production-grade automated database initialization module.
 * Hooks explicitly into the ApplicationReadyEvent to guarantee framework context stability.
 */
@Component
public class DataInitializer {

    private final AccountRepository accountRepository;

    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Spawns programmatically seeded financial ledger rows.
     * Runs strictly after the application container is fully initialized and operational.
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        if (!accountRepository.existsById(1L)) {
            Account clearingAccount = new Account("Enterprise Production Clearing Account", new BigDecimal("500000.0000"));
            clearingAccount.setId(1L);
            accountRepository.save(clearingAccount);
        }
    }
}