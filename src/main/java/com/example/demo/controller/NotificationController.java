package com.example.demo.controller;

import com.example.demo.event.RequestEvent;
import com.example.demo.event.ResponseDepositMinedEvent;
import com.example.demo.event.ResponseWithdrawPendingEvent;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;


/**
 * DepositMined Service 의 REST API
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final MonitoringService monitoringService;
    private final NotificationService notificationService;
    private final String valueTransferEventsHost;

    public NotificationController(final MonitoringService monitoringService,
                                  final NotificationService notificationService,
                                  @Value("${valueTransferEventsHost}") String valueTransferEventsHost){
        this.monitoringService = monitoringService;
        this.notificationService = notificationService;
        this.valueTransferEventsHost =valueTransferEventsHost;

    }
    private final String url = "http://localhost:3000/api/v2/eth/value-transfer-events";
    private final String size = "50";
    private final int page = 0;
    private final String updatedAtGte = Long.toString(System.currentTimeMillis()-600000);    //10분 전 데이터 조회하기

    private int idx = 0;
    /**
     * 3초마다 전체정보 조회 후 모니터링 서비스를 실행한다.
     * @throws JsonProcessingException
     */
    @GetMapping("/")
    @Scheduled(fixedDelay = 1000)
    public void getTransactionInfo() {


        Long start_time = System.currentTimeMillis();
        monitoringService.retrieveTransactionInfo(url, size, page, updatedAtGte);
        Long end_time = System.currentTimeMillis();

        System.out.println("No. "+idx+", Run-Time : "+(end_time-start_time)+"ms");
        idx++;
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
