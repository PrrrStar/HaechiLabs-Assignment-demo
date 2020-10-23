package com.example.demo.event;

import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import com.example.demo.domain.WithdrawConfirmed;
import com.example.demo.domain.WithdrawPending;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void withdrawPendingSend(final WithdrawPending withdrawPending, String exchange, String routingKey){
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                withdrawPending
        );
    }
    public void withdrawConfirmedSend(final WithdrawConfirmed withdrawConfirmed, String exchange, String routingKey){
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                withdrawConfirmed
        );
    }

}
