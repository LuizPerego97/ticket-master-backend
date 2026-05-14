package com.github.luizperego97.ticket_master_backend.repository;

import com.github.luizperego97.ticket_master_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
