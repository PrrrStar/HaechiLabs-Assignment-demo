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

    @RabbitListener(queues = "notification")
    public void receiveMessage(final Notification notification){
        System.out.println("Event Handler : "+notification.getTxId());
        log.info("notification 수신 : {}", notification.getTxId());
        try{
            notificationService.retrieveDepositMinedTx(notification);
        } catch (final Exception e){
            log.error("Error ",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }



}
