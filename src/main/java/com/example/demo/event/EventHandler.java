package com.example.demo.event;

import com.example.demo.controller.NotificationController;
import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import com.example.demo.domain.WithdrawConfirmed;
import com.example.demo.domain.WithdrawPending;
import com.example.demo.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventHandler {

    private NotificationService notificationService;

    EventHandler(final NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "depositMinedQueue")
    public void receivedMinedMessage(final DepositMined depositMined){

        try{
            notificationService.retrieveDepositMinedTx(depositMined);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    @RabbitListener(queues = "depositConfirmedQueue")
    public void receivedDepositConfirmedMessage(final DepositConfirmed depositConfirmed){

        try{
            notificationService.retrieveDepositConfirmedTx(depositConfirmed);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    @RabbitListener(queues = "withdrawPendingQueue")
    public void receivedWithdrawPendingMessage(final WithdrawPending withdrawPending){

        try{
            notificationService.retrieveWithdrawPendingTx(withdrawPending);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    @RabbitListener(queues = "withdrawConfirmedQueue")
    public void receivedWithdrawConfirmedMessage(final WithdrawConfirmed withdrawConfirmed){

        try{
            notificationService.retrieveWithdrawConfirmedTx(withdrawConfirmed);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
