package com.example.demo.service;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface NotificationService {


    /**
     * 모든 트랜잭션 조회
     * @return
     * @throws JsonProcessingException
     */
    List<TransferEventResultDTO.Results> retrieveAllTransactionResult() throws JsonProcessingException;

    /**
     * 입출금 타입과 트랜잭션 상태로 트랜잭션 조회
     * @return
     */
    List<Transaction> retrieveTransactionByTypeAndStatus();



    /**
     * 입출금 타입으로 트랜잭션 조회
     * @param transferType
     * @return
     */
    List<Transaction> retrieveTransactionByType(String transferType);

}
