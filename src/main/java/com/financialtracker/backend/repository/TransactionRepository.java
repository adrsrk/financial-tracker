package com.financialtracker.backend.repository;

import com.financialtracker.backend.entity.Transaction;
import com.financialtracker.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountUser(User user);
}
