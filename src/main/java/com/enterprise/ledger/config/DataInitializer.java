package com.enterprise.ledger.config;

import com.enterprise.ledger.model.Account;
import com.enterprise.ledger.repository.AccountRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * Production-hardened database seed initializer module.
 * Delegates primary key allocation completely to native sequence managers to pass Jpa lifecycle criteria.
 */
@Component
public class DataInitializer {

    private final AccountRepository accountRepository;

    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Seeds base financial clearing parameters safely after database migrations settle.
     * Respects JPA persistence boundary rules to avoid context crashing loops.
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        // Safe check using a query probe before generating rows
        if (accountRepository.count() == 0) {
            // Leave ID completely blank to let accounts_id_seq allocate values natively
            Account clearingAccount = new Account("Enterprise Production Clearing Account", new BigDecimal("500000.0000"));
            accountRepository.save(clearingAccount);
        }
    }
}