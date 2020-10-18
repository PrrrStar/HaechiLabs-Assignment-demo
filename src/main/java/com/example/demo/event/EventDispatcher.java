package com.example.demo.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {
    private RabbitTemplate rabbitTemplate;

    private String notification_exchange;

    private String depositMinedRoutingKey;
    private String depositReorgedRoutingKey;
    private String depositConfirmeRoutingKey;
    private String withdrawPendingRoutingKey;
    private String withdrawConfirmedRoutingKey;

    @Autowired
    EventDispatcher(final RabbitTemplate rabbitTemplate,
                    @Value("${notification.exchange}") final String notification_exchange,
                    @Value("${depositMinedRoutingKey}") final String depositMinedRoutingKey,
                    @Value("${depositReorgedRoutingKey}") final String depositReorgedRoutingKey,
                    @Value("${depositConfirmeRoutingKey}") final String depositConfirmeRoutingKey,
                    @Value("${withdrawPendingRoutingKey}") final String withdrawPendingRoutingKey,
                    @Value("${withdrawConfirmedRoutingKey}") final String withdrawConfirmedRoutingKey
                    ){
        this.rabbitTemplate=rabbitTemplate;
        this.notification_exchange=notification_exchange;
        this.depositMinedRoutingKey=depositMinedRoutingKey;
        this.depositReorgedRoutingKey=depositReorgedRoutingKey;
        this.depositConfirmeRoutingKey=depositConfirmeRoutingKey;
        this.withdrawPendingRoutingKey=withdrawPendingRoutingKey;
        this.withdrawConfirmedRoutingKey=withdrawConfirmedRoutingKey;
    }



}
