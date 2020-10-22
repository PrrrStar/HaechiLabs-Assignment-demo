package com.example.demo.service;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface NotificationService {



    /**
     * 모든 트랜잭션 조회
     * @return
     */
    List<TransferEventResultDTO.Results> retrieveAllTxInfo() throws JsonProcessingException;


    List<TransferEventResultDTO.Results> retrieveDepositMinedTx(Notification notification);
    List<TransferEventResultDTO.Results> retrieveDepositConfirmedTx(Notification notification);
    List<TransferEventResultDTO.Results> retrieveWithdrawPendingTx(Notification notification);
    List<TransferEventResultDTO.Results> retrieveWithdrawConfirmedTx(Notification notification);

}
