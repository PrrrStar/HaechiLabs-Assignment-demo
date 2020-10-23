package com.example.demo.event;

import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    EventDispatcher(final RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void depositMindedSend(final DepositMined depositMined, String exchange, String routingKey){
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                depositMined
        );
    }
    public void depositConfirmedSend(final DepositConfirmed depositConfirmed, String exchange, String routingKey){
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                depositConfirmed
        );
    }

}
