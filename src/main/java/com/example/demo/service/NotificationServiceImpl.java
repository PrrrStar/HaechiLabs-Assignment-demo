package com.example.demo.service;

import com.example.demo.client.TransferEventClient;
import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.Transaction;
import com.example.demo.domain.TransactionStatus;
import com.example.demo.domain.TransferType;
import com.example.demo.repository.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private NotificationRepository notificationRepository;
    private TransferEventClient transferEventClient;

    NotificationServiceImpl(NotificationRepository notificationRepository,
                            TransferEventClient transferEventClient){
        this.notificationRepository = notificationRepository;
        this.transferEventClient = transferEventClient;
    }

    /**
     * 입출금 타입과 트랜잭션 상태로 필터링
     * @param transferType
     * @param status
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public List<TransferEventResultDTO.Results> retrieveTxByTransferTypeAndStatus(String transferType, String status)
            throws JsonProcessingException {

        List<TransferEventResultDTO.Results> results = transferEventClient.retrieveTransferEventResultDTO().getResults();
        List<TransferEventResultDTO.Results> transactions = results.stream()
                .filter(t -> t.getTransferType().contains(transferType))
                .filter(s -> s.getStatus().contains(status))
                .collect(Collectors.toList());

        return transactions;
    }


    /**
     * 입출금 타입으로 필터링
     * @param transferType
     * @return
     */
    @Override
    public List<TransferEventResultDTO.Results> retrieveTxByTransferType(String transferType)
            throws JsonProcessingException {

        List<TransferEventResultDTO.Results> results = transferEventClient.retrieveTransferEventResultDTO().getResults();
        List<TransferEventResultDTO.Results> transactions = results.stream()
                .filter(t -> t.getTransferType().contains(transferType))
                .collect(Collectors.toList());

        return transactions;
    }

    /**
     * 전체 트랜잭션 조회
     * @return
     */
    @Override
    public List<TransferEventResultDTO.Results> retrieveTxALL()
            throws JsonProcessingException {

        List<TransferEventResultDTO.Results> results = transferEventClient.retrieveTransferEventResultDTO().getResults();

        return results;
    }

}
