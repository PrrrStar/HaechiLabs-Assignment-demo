package com.example.demo.controller;


import com.example.demo.event.NotificationDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class TestNotificationsController {


/**
    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId());
    }
    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.remove(stompHeaderAccessor.getSessionId());
    }
    **/
}


