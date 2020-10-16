package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/deposit_minded")
    public List<Transaction> getDepositMindedTransaction(
            @RequestParam(value = "transfer_type") final String transferType,
            @RequestParam(value = "status") final String status){
        return notificationService.retrieveTransactionByTypeAndStatus(transferType,status);
    }


    @GetMapping("/deposit_reorged")
    public List<Transaction> getDepositReorgedTransaction(
            @RequestParam(value = "transfer_type") final String transferType,
            @RequestParam(value = "tx_status") final String status){
        return notificationService.retrieveTransactionByTypeAndStatus(transferType,status);
    }

    @GetMapping("/deposit_confirm")
    public List<Transaction> getDepositConfirmTransaction(
            @RequestParam(value = "transfer_type") final String transferType,
            @RequestParam(value = "tx_status") final String status){
        return notificationService.retrieveTransactionByTypeAndStatus(transferType,status);
    }


    @GetMapping("/withdraw_pending")
    public List<Transaction> getWithdrawPendingTransaction(
            @RequestParam(value = "transfer_type") final String transferType,
            @RequestParam(value = "tx_status") final String status){
        return notificationService.retrieveTransactionByTypeAndStatus(transferType,status);
    }

    @GetMapping("/withdraw_confirmed")
    public List<Transaction> getWithdrawConfirmedTransaction(
            @RequestParam(value = "transfer_type") final String transferType,
            @RequestParam(value = "tx_status") final String status){
        return notificationService.retrieveTransactionByTypeAndStatus(transferType,status);
    }


    @GetMapping("/deposit")
    public List<Transaction> getDepositTransaction(
            @RequestParam(value = "transfer_type") final String transferType){
        return notificationService.retrieveTransactionByType(transferType);
    }

    @GetMapping("/withdraw")
    public List<Transaction> getWithdrawTransaction(
            @RequestParam(value = "transfer_type") final String transferType){
        return notificationService.retrieveTransactionByType(transferType);
    }

}
