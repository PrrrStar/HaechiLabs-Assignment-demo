package com.example.demo.controller;

import com.example.demo.domain.DepositMindedDomain;
import com.example.demo.service.DepositMindedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications/deposit_mined")
public class DepositMindedController {


    private final DepositMindedService depositMindedService;

    public DepositMindedController(final DepositMindedService depositMindedService){
        this.depositMindedService = depositMindedService;
    }

//    @GetMapping
//    public List<DepositMindedDomain> getDepositMinded(){
//        return depositMindedService.getCurrentDepositMinded;
//    }
}
