package com.example.demo.service;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.Transaction;
import com.example.demo.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private NotificationRepository notificationRepository;
    private TransferEventResultDTO transferEventResultDTO;

    NotificationServiceImpl(NotificationRepository notificationRepository,
                            TransferEventResultDTO transferEventResultDTO){
        this.notificationRepository = notificationRepository;
        this.transferEventResultDTO = transferEventResultDTO;
    }


    /**
     * 입출금 타입과 트랜잭션 상태로 찾기
     * @param transferType
     * @param txStatus
     * @return
     */
    @Override
    public List<Transaction> retrieveTransactionByTypeAndStatus(String transferType, String txStatus) {
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
