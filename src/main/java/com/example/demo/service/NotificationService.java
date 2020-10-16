package com.example.demo.service;

import com.example.demo.domain.Transaction;

import java.util.List;

public interface NotificationService {



    /**
     * 해당 트랜잭션의 통계 조회
     * @param transactionId
     * @return
     */
    List<Transaction> findByTransferType(String transferType);
}
