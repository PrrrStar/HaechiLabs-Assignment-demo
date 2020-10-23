package com.example.demo.service;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface NotificationService {



    /**
     * 모든 트랜잭션 조회
     * @return
     */
    List<TransferEventResultDTO.Results> retrieveAllTxInfo() throws JsonProcessingException;


    List<DepositMined> retrieveDepositMinedTx(DepositMined depositMined);
    List<DepositConfirmed> retrieveDepositConfirmedTx(DepositConfirmed depositConfirmed);
    List<DepositMined> retrieveWithdrawPendingTx(DepositMined depositMined);
    List<DepositMined> retrieveWithdrawConfirmedTx(DepositMined depositMined);

}
