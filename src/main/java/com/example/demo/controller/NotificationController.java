package com.example.demo.controller;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Notification Service 의 REST API
 */

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    public NotificationController(final NotificationService notificationService){
        this.notificationService = notificationService;

    }

    @GetMapping("/")
    public List<TransferEventResultDTO.Results> getAllTx() throws JsonProcessingException {
        return notificationService.retrieveTxALL();
    }

    @Scheduled(fixedDelay = 100)
    @PostMapping("/deposit_mined")
    public List<TransferEventResultDTO.Results> getDepositMinedTx() throws JsonProcessingException {
        return notificationService.retrieveTxByTransferTypeAndStatus("DEPOSIT","MINED");
    }

    @Scheduled(fixedDelay = 100)
    @PostMapping("/deposit_reorged")
    public List<TransferEventResultDTO.Results> getDepositReorgedTx() throws JsonProcessingException {
        return notificationService.retrieveTxByTransferTypeAndStatus("DEPOSIT","REPLACED");
    }
    @Scheduled(fixedDelay = 100)
    @PostMapping("/deposit_confirm")
    public List<TransferEventResultDTO.Results> getDepositConfirmTx() throws JsonProcessingException {
        return notificationService.retrieveTxByTransferTypeAndStatus("DEPOSIT","CONFIRMED");
    }
    @Scheduled(fixedDelay = 100)
    @PostMapping("/withdraw_pending")
    public List<TransferEventResultDTO.Results> getWithdrawPendingTx() throws JsonProcessingException {
        return notificationService.retrieveTxByTransferTypeAndStatus("WITHDRAWAL","PENDING");
    }
    @Scheduled(fixedDelay = 100)
    @PostMapping("/withdraw_confirmed")
    public List<TransferEventResultDTO.Results> getWithdrawConfirmedTx() throws JsonProcessingException {
        return notificationService.retrieveTxByTransferTypeAndStatus("WITHDRAWAL","CONFIRMED");
    }



    @GetMapping("/deposit")
    public List<TransferEventResultDTO.Results> getDepositTx() throws JsonProcessingException {
        return notificationService.retrieveTxByTransferType("DEPOSIT");
    }

    @GetMapping("/withdraw")
    public List<TransferEventResultDTO.Results> getWithdrawTx() throws JsonProcessingException {
        return notificationService.retrieveTxByTransferType("WITHDRAWAL");
    }


}
