package com.example.demo.event;

import com.example.demo.domain.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Rabbit MQ 메세지 전송자 관련 클래스입니다.
 * 각 큐별 send method 를 달리해서 다른 정보를 보낼 수 있습니다.
 */
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

    public void depositReorgedSend(final DepositReorged depositReorged, String exchange, String routingKey) {
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                depositReorged
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
