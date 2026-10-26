package com.enterprise.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Core application entry point for the production-grade Financial Ledger Service.
 * Formulated under Java 25 LTS running natively optimized on Project Loom Virtual Threads.
 */
@SpringBootApplication
public class LedgerApplication {

    public static void main(String[] args) {
        // Initializes the container and boot-straps the high-concurrency microservice engine
        SpringApplication.run(LedgerApplication.class, args);
    }
}
