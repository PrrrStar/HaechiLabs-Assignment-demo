package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {


    private final DepositMindedService depositMindedService;
    private final DepositReorgedService depositReorgedService;
    private final DepositConfirmService depositConfirmService;

    private final WithdrawPendingService withdrawPendingService;
    private final WithDrawConfirmedService withDrawConfirmedService;

    public NotificationController(final DepositMindedService depositMindedService,
                                  final DepositReorgedService depositReorgedService,
                                  final DepositConfirmService depositConfirmService,
                                  final WithdrawPendingService withdrawPendingService,
                                  final WithDrawConfirmedService withDrawConfirmedService){
        this.depositMindedService = depositMindedService;
        this.depositReorgedService = depositReorgedService;
        this.depositConfirmService = depositConfirmService;
        this.withdrawPendingService = withdrawPendingService;
        this.withDrawConfirmedService = withDrawConfirmedService;
    }


    @GetMapping("/deposit_mined")
    public List<DepositMindedDomain> getDepositMinded(
            @RequestParam(value = "transferType", required = false) final String transferType,
            @RequestParam(value= "status", required = false) final String status){
        return depositMindedService.retrieveDepositMinded(transferType, status);
    }

    @GetMapping("/deposit_reorged")
    public List<DepositReorgedService> getDepositReorged(
            @RequestParam(value = "transferType", required = false) final String transferType,
            @RequestParam(value= "status", required = false) final String status){
        return depositReorgedService.retrieveDepositReorged(transferType, status);
    }
    @GetMapping("/deposit_confirm")
    public List<DepositConfirmService> getDepositConfirm(
            @RequestParam(value = "transferType", required = false) final String transferType,
            @RequestParam(value= "status", required = false) final String status){
        return depositConfirmService.retrieveDepositConfirm(transferType, status);
    }
    @GetMapping("/withdraw_pending")
    public List<WithdrawPendingDomain> getWithdrawPending(
            @RequestParam(value = "transferType", required = false) final String transferType,
            @RequestParam(value= "status", required = false) final String status){
        return withdrawPendingService.retrieveWithdrawPending(transferType, status);
    }
    @GetMapping("/withdraw_confirmed")
    public List<WithdrawConfirmedDomain> getWithDrawConfirmed(
            @RequestParam(value = "transferType", required = false) final String transferType,
            @RequestParam(value= "status", required = false) final String status){
        return withDrawConfirmedService.retrieveWithDrawConfirmed(transferType, status);
    }


}
