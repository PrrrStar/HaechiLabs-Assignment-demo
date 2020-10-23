package com.example.demo.controller;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import com.example.demo.domain.WithdrawConfirmed;
import com.example.demo.domain.WithdrawPending;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * DepositMined Service 의 REST API
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService){
        this.notificationService = notificationService;

    }

    @GetMapping("/")
    @Scheduled(fixedDelay = 3000)
    public List<TransferEventResultDTO.Results> getAllTx() throws JsonProcessingException {
        return notificationService.retrieveAllTxInfo();
    }


    @PostMapping("/deposit_mined")
    public List<DepositMined> getDepositMinedTx(DepositMined depositMined){
        return notificationService.retrieveDepositMinedTx(depositMined);
    }
    @PostMapping("/deposit_reorged")
    public List<TransferEventResultDTO.Results> getDepositReorgedTx() throws JsonProcessingException {
        return notificationService.retrieveAllTxInfo();
    }
    @PostMapping("/deposit_confirm")
    public List<DepositConfirmed> getDepositConfirmTx(DepositConfirmed depositConfirmed){
        return notificationService.retrieveDepositConfirmedTx(depositConfirmed);
    }
    @PostMapping("/withdraw_pending")
    public List<WithdrawPending> getWithdrawPendingTx(WithdrawPending withdrawPending){
        return notificationService.retrieveWithdrawPendingTx(withdrawPending);
    }
    @PostMapping("/withdraw_confirmed")
    public List<WithdrawConfirmed> getWithdrawConfirmedTx(WithdrawConfirmed withdrawConfirmed){
        return notificationService.retrieveWithdrawConfirmedTx(withdrawConfirmed);
    }


}
