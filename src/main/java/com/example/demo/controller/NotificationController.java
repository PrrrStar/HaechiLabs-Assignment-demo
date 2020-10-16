package com.example.demo.controller;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.*;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<TransferEventResultDTO.Results> getTest() throws JsonProcessingException {
        return notificationService.retrieveAllTransactionResult();
    }

    @GetMapping("/deposit_minded")
    public List<Transaction> getDepositMindedTransaction(){
        return notificationService.retrieveTransactionByTypeAndStatus();
    }


    @GetMapping("/deposit_reorged")
    public List<Transaction> getDepositReorgedTransaction(){
        return notificationService.retrieveTransactionByTypeAndStatus();
    }

    @GetMapping("/deposit_confirm")
    public List<Transaction> getDepositConfirmTransaction(){
        return notificationService.retrieveTransactionByTypeAndStatus();
    }


    @GetMapping("/withdraw_pending")
    public List<Transaction> getWithdrawPendingTransaction(){
        return notificationService.retrieveTransactionByTypeAndStatus();
    }

    @GetMapping("/withdraw_confirmed")
    public List<Transaction> getWithdrawConfirmedTransaction(){
        return notificationService.retrieveTransactionByTypeAndStatus();
    }

/*

    @GetMapping("/deposit")
    public List<Transaction> getDepositTransaction(){
        return notificationService.retrieveTransactionByType();
    }

    @GetMapping("/withdraw")
    public List<Transaction> getWithdrawTransaction(
            @RequestParam(value = "transferType") final String transferType){
        return notificationService.retrieveTransactionByType(transferType);
    }
*/
}
