package com.example.demo.controller;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
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
    @Scheduled(fixedDelay = 10000)
    public List<TransferEventResultDTO.Results> getAllTx() throws JsonProcessingException {
        return notificationService.retrieveAllTxInfo();
    }


    @PostMapping("/deposit_mined")
    public List<DepositMined> getDepositMinedTx(DepositMined depositMined) throws JsonProcessingException {
        return notificationService.retrieveDepositMinedTx(depositMined);
    }

    @PostMapping("/deposit_reorged")
    public List<TransferEventResultDTO.Results> getDepositReorgedTx() throws JsonProcessingException {
        return notificationService.retrieveAllTxInfo();
    }
    @PostMapping("/deposit_confirm")
    public List<DepositConfirmed> getDepositConfirmTx(DepositConfirmed depositConfirmed) throws JsonProcessingException {
        return notificationService.retrieveDepositConfirmedTx(depositConfirmed);
    }
    @PostMapping("/withdraw_pending")
    public List<DepositMined> getWithdrawPendingTx(DepositMined depositMined) throws JsonProcessingException {
        return notificationService.retrieveWithdrawPendingTx(depositMined);
    }
    @PostMapping("/withdraw_confirmed")
    public List<DepositMined> getWithdrawConfirmedTx(DepositMined depositMined) throws JsonProcessingException {
        return notificationService.retrieveWithdrawConfirmedTx(depositMined);
    }


}
