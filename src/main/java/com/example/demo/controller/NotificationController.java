package com.example.demo.controller;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.WithdrawConfirmed;
import com.example.demo.domain.WithdrawPending;
import com.example.demo.event.RequestEvent;
import com.example.demo.event.ResponseDepositMinedEvent;
import com.example.demo.event.ResponseWithdrawPendingEvent;
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

    private final MonitoringService monitoringService;
    private final NotificationService notificationService;

    public NotificationController(final MonitoringService monitoringService,
                                  final NotificationService notificationService){
        this.monitoringService = monitoringService;
        this.notificationService = notificationService;

    }

    /**
     * 3초마다 전체정보 조회 후 모니터링 서비스를 실행한다.
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/")
    @Scheduled(fixedDelay = 1000)
    public List<TransferEventResultDTO.Results> getAllTx() throws JsonProcessingException {
        return monitoringService.retrieveAllTxInfo();
    }



    @PostMapping("/deposit_mined")
    public ResponseDepositMinedEvent postDepositMinedTx(@RequestBody RequestEvent requestEvent){
        return notificationService.retrieveDepositMinedTx(requestEvent);
    }
    @PostMapping("/deposit_reorged")
    public void postDepositReorgedTx(@RequestBody RequestEvent requestEvent) {
        notificationService.retrieveDepositReorgedTx(requestEvent);
    }
    @PostMapping("/deposit_confirm")
    public void postDepositConfirmTx(@RequestBody RequestEvent requestEvent){
        notificationService.retrieveDepositConfirmedTx(requestEvent);
    }
    @PostMapping("/withdraw_pending")
    public ResponseWithdrawPendingEvent postWithdrawPendingTx(@RequestBody RequestEvent requestEvent){
        return notificationService.retrieveWithdrawPendingTx(requestEvent);
    }
    @PostMapping("/withdraw_confirmed")
    public void postWithdrawConfirmedTx(@RequestBody RequestEvent requestEvent){
        notificationService.retrieveWithdrawConfirmedTx(requestEvent);
    }


}
