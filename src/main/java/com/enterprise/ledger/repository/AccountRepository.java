package com.enterprise.ledger.repository;

import com.enterprise.ledger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Enterprise Data Access Object interface for the Account entity model.
 * Connects directly to our optimized HikariCP connection pool infrastructure.
 * Execution pathways are non-blocking when triggered by JVM Virtual Threads.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Inherits standard high-concurrency CRUD mechanisms securely from JpaRepository
}
