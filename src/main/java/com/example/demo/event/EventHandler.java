package com.example.demo.event;

import com.example.demo.controller.NotificationController;
import com.example.demo.domain.*;
import com.example.demo.service.MonitoringService;
import com.example.demo.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * RabbitLitenr 를 통해 Dispatcher 로 보낸 정보들을 실시간으로 받습니다.
 * 큐 이름이 일치해야 하며 받은 정보를 보낼 클래스를 설정할 수 있습니다.
 */
@Slf4j
@Component
public class EventHandler {

    private MonitoringService monitoringService;
    private NotificationService notificationService;

    private NotificationController controller;

    EventHandler(final MonitoringService monitoringService,
                 final NotificationService notificationService,
                 final NotificationController controller){
        this.monitoringService = monitoringService;
        this.notificationService = notificationService;
        this.controller = controller;

    }

    /**
     * depositMinedQueue 로 넘어온 데이터를 Listen 합니다.
     * 그 후 controller 의 postDepositMinedTx 매서드에 그 값을 전달합니다.
     * @param depositMined
     */
    @RabbitListener(queues = "depositMinedQueue")
    public void receivedDepositMinedMessage(final DepositMined depositMined){

        try{
            controller.postDepositMinedTx(depositMined);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    @RabbitListener(queues = "depositReorgedQueue")
    public void receivedDepositReorgedMessage(final DepositReorged depositReorged){

        try{
            controller.postDepositReorgedTx(depositReorged);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "depositConfirmedQueue")
    public void receivedDepositConfirmedMessage(final DepositConfirmed depositConfirmed){

        try{
            controller.postDepositConfirmTx(depositConfirmed);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    @RabbitListener(queues = "withdrawPendingQueue")
    public void receivedWithdrawPendingMessage(final WithdrawPending withdrawPending){

        try{
            controller.postWithdrawPendingTx(withdrawPending);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    @RabbitListener(queues = "withdrawConfirmedQueue")
    public void receivedWithdrawConfirmedMessage(final WithdrawConfirmed withdrawConfirmed){

        try{
            controller.postWithdrawConfirmedTx(withdrawConfirmed);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }



}
