package com.example.demo.repository;

import com.example.demo.domain.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Transaction, Long>, QuerydslPredicateExecutor<Transaction> {
    List<Transaction> findByTransferType(String transfer_type);
}
