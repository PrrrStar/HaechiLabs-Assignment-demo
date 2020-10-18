package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // enableSimpleBroker
    // 해당 topic 을 구독하고 있는 n명의 클라이언트에게 메세지를 전달한다.
    // queue ->1:1

    // setApplicationDestinationPrefixes
    // 도착경로 prefix
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic","/queue");
        config.setApplicationDestinationPrefixes("/app");
    }


    // 클라이언트에서 WebSocket 을 연결할 api 를 설정한다.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websockethandler").withSockJS();
    }
}
