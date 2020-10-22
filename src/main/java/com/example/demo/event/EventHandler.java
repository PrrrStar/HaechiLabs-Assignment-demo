package com.example.demo.event;

import com.example.demo.domain.Notification;
import com.example.demo.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
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
    public void receivedMinedMessage(final Notification notification){

        try{
            notificationService.retrieveDepositMinedTx(notification);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    @RabbitListener(queues = "depositConfirmedQueue")
    public void receivedDepositConfirmedMessage(final Notification notification){

        try{
            notificationService.retrieveDepositConfirmedTx(notification);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "withdrawPendingQueue")
    public void receivedWithdrawPendingMessage(final Notification notification){

        try{
            notificationService.retrieveWithdrawPendingTx(notification);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    @RabbitListener(queues = "withdrawConfirmedQueue")
    public void receivedWithdrawConfirmedMessage(final Notification notification){

        try{
            notificationService.retrieveWithdrawConfirmedTx(notification);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
