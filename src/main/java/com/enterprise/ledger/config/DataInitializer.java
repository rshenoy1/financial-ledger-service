package com.enterprise.ledger.config;

import com.enterprise.ledger.model.Account;
import com.enterprise.ledger.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * Production-grade automated database record initialization wrapper.
 * Validates and seats base ledger balances at container boot-time before traffic routes open.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Enforce baseline clearing account presence programmatically if database volumes reset
        if (!accountRepository.existsById(1L)) {
            Account clearingAccount = new Account("Enterprise Production Clearing Account", new BigDecimal("500000.0000"));
            clearingAccount.setId(1L);
            accountRepository.save(clearingAccount);
        }
    }
}