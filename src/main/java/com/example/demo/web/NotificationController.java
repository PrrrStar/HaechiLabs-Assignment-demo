package com.example.demo.web;

import com.example.demo.domain.*;
import com.example.demo.domain.ResponseMined;
import com.example.demo.domain.ResponsePending;
import com.example.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;


/**
 * 알림 어플리케이션의 컨트롤러입니다.
 */
@Slf4j
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


    /**
     * 정보를 Tracking 할 때 필요한 Request URL Components 입니다. Client 에서 위 값들을 build 해줍니다.
     */
    private final String size = "100";
    private final int page = 0;
    private final String status = "";
    private final String walletId = "";
    private final String masterWalletId ="";
    private final String updatedAtGte = Long.toString(System.currentTimeMillis()-600000);    // 10분 전 데이터 조회하기

    private int idx = 0;

    /**
     * 1초마다 value-transfer-event api 서버를 Hooking 한 후 모니터링 서비스를 실행합니다.
     */
    @Async
    @Scheduled(fixedRate = 300, initialDelay = 2000)
    public void getTransactionInfo() {

        Long start_time = System.currentTimeMillis();
        try{
            monitoringService.retrieveTransactionInfo(valueTransferEventsHost, size, page, status, walletId, masterWalletId, updatedAtGte);
        } catch(Exception e){
            log.error(String.valueOf(e));
        }
        Long end_time = System.currentTimeMillis();

        log.info("No."+idx+"\tRun-Time : "+(end_time-start_time)+"ms");
        idx++;
    }

    @PostMapping("/deposit_mined")
    public ResponseMined postDepositMinedTx(@RequestBody DepositMined depositMined){
        return notificationService.retrieveDepositMinedTx(depositMined);
    }
    @PostMapping("/deposit_reorged")
    public void postDepositReorgedTx(@RequestBody DepositReorged depositReorged) {
        notificationService.retrieveDepositReorgedTx(depositReorged);
    }
    @PostMapping("/deposit_confirm")
    public void postDepositConfirmTx(@RequestBody DepositConfirmed depositConfirmed){
        notificationService.retrieveDepositConfirmedTx(depositConfirmed);
    }
    @PostMapping("/withdraw_pending")
    public ResponsePending postWithdrawPendingTx(@RequestBody WithdrawPending withdrawPending){
        return notificationService.retrieveWithdrawPendingTx(withdrawPending);
    }
    @PostMapping("/withdraw_confirmed")
    public void postWithdrawConfirmedTx(@RequestBody WithdrawConfirmed withdrawConfirmed){
        notificationService.retrieveWithdrawConfirmedTx(withdrawConfirmed);
    }


}
