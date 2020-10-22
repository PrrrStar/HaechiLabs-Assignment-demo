package com.example.demo.event;

import com.example.demo.domain.Notification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {
    private RabbitTemplate rabbitTemplate;
    private String notificationExchange;
    private String notificationRoutingKey;

    @Autowired
    EventDispatcher(final RabbitTemplate rabbitTemplate,
                    @Value("${notification.exchange}") String notificationExchange,
                    @Value("${notification.key}") String notificationRoutingKey){
        this.rabbitTemplate = rabbitTemplate;
        this.notificationExchange = notificationExchange;
        this.notificationRoutingKey = notificationRoutingKey;
    }

    public void send(final Notification notification){
        rabbitTemplate.convertAndSend(
                notificationExchange,
                notificationRoutingKey,
                notification
        );
    }

}
