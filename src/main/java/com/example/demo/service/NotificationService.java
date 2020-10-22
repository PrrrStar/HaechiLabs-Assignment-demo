package com.example.demo.service;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface NotificationService {

    /**
     * 입출금 타입과 트랜잭션 상태로 트랜잭션 조회
     * @param transferType
     * @param status
     * @return
     * @throws JsonProcessingException
     */
    List<TransferEventResultDTO.Results> retrieveTxByTransferTypeAndStatus(String transferType, String status)
            throws JsonProcessingException;


    /**
     * 입출금 타입으로 트랜잭션 조회
     * @param transferType
     */
    List<TransferEventResultDTO.Results> retrieveTxByTransferType(String transferType) throws JsonProcessingException;


    /**
     * 모든 트랜잭션 조회
     * @return
     */
    List<TransferEventResultDTO.Results> retrieveTxALL() throws JsonProcessingException;

}
