package com.example.demo.service;

import com.example.demo.domain.Transaction;

public interface NotificationService {



    /**
     * 해당 트랜잭션의 통계 조회
     * @param transactionId
     * @return
     */
    Transaction retrieveStatsForTransaction(String transactionId);
}
