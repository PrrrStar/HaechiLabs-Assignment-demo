package com.example.demo.service;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import com.example.demo.domain.WithdrawPending;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface MonitoringService {

    /**
     * 모든 트랜잭션 조회
     * @return
     */
    List<TransferEventResultDTO.Results> retrieveAllTxInfo() throws JsonProcessingException;
}
