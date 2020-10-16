package com.example.demo.service;

import com.example.demo.client.TransferEventClient;
import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.Transaction;
import com.example.demo.repository.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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


    String transferType;
    String txStatus;

    /**
     * 입출금 타입과 트랜잭션 상태로 찾기
     * @return
     */
    @Override
    public List<TransferEventResultDTO.Results> retrieveAllTransactionResult() throws JsonProcessingException {
        TransferEventResultDTO.Results results = new TransferEventResultDTO.Results();

        List<TransferEventResultDTO.Results> response = transferEventClient.retrieveTransferEventResultDTO().getResults();

        System.out.println(response);
        return response;
    }


    @Override
    public List<Transaction> retrieveTransactionByTypeAndStatus() {

        if (transferType == "DEPOSIT"){
            if(txStatus == "MINDED"){
                log.info("입출금 타입 {}\n트랜잭션 상태 {}",transferType,txStatus);
                return notificationRepository.findByTransferTypeAndTxStatus("DEPOSIT","MINDED");
            }else if(txStatus == "REPLACED"){
                return notificationRepository.findByTransferTypeAndTxStatus("DEPOSIT","REPLACED");
            }else if(txStatus == "CONFIRMED") {
                return notificationRepository.findByTransferTypeAndTxStatus("DEPOSIT","CONFIRMED");
            }else {
                return null;
            }
        }else if(transferType == "WITHDRAWAL"){
            if(txStatus == "PENDING"){
                return notificationRepository.findByTransferTypeAndTxStatus("WITHDRAWAL","PENDING");
            }else if(txStatus == "CONFIRMED") {
                return notificationRepository.findByTransferTypeAndTxStatus("WITHDRAWAL","CONFIRMED");
            }else {
                return null;
            }
        }else{
            return null;
        }
    }




    /**
     * 입출금 타입으로 찾기
     * @param transferType
     * @return
     */
    @Override
    public List<Transaction> retrieveTransactionByType(String transferType) {
        if (transferType == "DEPOSIT"){
            return notificationRepository.findByTransferType("DEPOSIT");
        }else if(transferType == "WITHDRAWAL"){
            return notificationRepository.findByTransferType("WITHDRAWAL");
        }else{
            return null;
        }

    }




}
