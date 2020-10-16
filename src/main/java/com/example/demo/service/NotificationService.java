package com.example.demo.service;

import com.example.demo.domain.Transaction;

import java.util.List;

public interface NotificationService {

    /**
     * 입출금 타입을 가진 트랜잭션 조회
     * @param transferType
     * @return
     */
    List<Transaction> retrieveTransactionByType(String transferType);


    /**
     * 입출금 타입, 상태를 가진 트랜잭션 조회
     * @param transferType
     * @param txStatus
     * @return
     */
    List<Transaction> retrieveTransactionByTypeAndStatus(String transferType, String txStatus);
}
