package com.example.demo.service;

import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import com.example.demo.domain.WithdrawConfirmed;
import com.example.demo.domain.WithdrawPending;
import com.example.demo.event.RequestEvent;
import com.example.demo.event.ResponseDepositMinedEvent;
import com.example.demo.event.ResponseWithdrawPendingEvent;

import java.util.List;

public interface NotificationService {


    ResponseDepositMinedEvent retrieveDepositMinedTx(RequestEvent requestEvent);
    void retrieveDepositReorgedTx(RequestEvent requestEvent);
    void retrieveDepositConfirmedTx(RequestEvent requestEvent);
    ResponseWithdrawPendingEvent retrieveWithdrawPendingTx(RequestEvent requestEvent);
    void retrieveWithdrawConfirmedTx(RequestEvent requestEvent);



    List<DepositMined> dm(DepositMined depositMined);
}
