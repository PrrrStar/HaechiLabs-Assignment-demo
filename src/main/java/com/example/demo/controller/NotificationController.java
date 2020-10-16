package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Notification Service 의 REST API
 */

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @GetMapping("/deposit_mined")
    public List<Transaction> getStatsForTransaction(
            @RequestParam(value = "transferType") final String transferType){
        return notificationService.findByTransferType(transferType);
    }
//
//    @GetMapping("/deposit_reorged")

//    @GetMapping("/deposit_confirm")

//    @GetMapping("/withdraw_pending")

//    @GetMapping("/withdraw_confirmed")

}
