package com.example.demo.controller;

import com.example.demo.service.DepositMindedService;
import org.springframework.web.bind.annotation.GetMapping;

public class DepositMindedController {


    private final DepositMindedService depositMindedService;

    public DepositMindedController(final DepositMindedService depositMindedService){
        this.depositMindedService = depositMindedService;
    }

}
