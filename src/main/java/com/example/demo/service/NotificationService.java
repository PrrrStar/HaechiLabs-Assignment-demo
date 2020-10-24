package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.event.ResponseMinedEvent;
import com.example.demo.event.ResponsePendingEvent;


public interface NotificationService {


    ResponseMinedEvent retrieveDepositMinedTx(DepositMined depositMined);
    void retrieveDepositReorgedTx(DepositReorged requestEvent);
    void retrieveDepositConfirmedTx(DepositConfirmed requestEvent);
    ResponsePendingEvent retrieveWithdrawPendingTx(WithdrawPending requestEvent);
    void retrieveWithdrawConfirmedTx(WithdrawConfirmed requestEvent);

}
