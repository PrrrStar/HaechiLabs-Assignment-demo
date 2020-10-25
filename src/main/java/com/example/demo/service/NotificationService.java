package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.event.ResponseMinedEvent;
import com.example.demo.event.ResponsePendingEvent;


public interface NotificationService {


    /**
     * Controller 에서 호출하는 메서드들
     * @param depositMined  EventHandler 에서 받은, 혹은 json request 로 들어온 정보를 받습니다.
     * @return
     */
    ResponseMinedEvent retrieveDepositMinedTx(DepositMined depositMined);
    void retrieveDepositReorgedTx(DepositReorged requestEvent);
    void retrieveDepositConfirmedTx(DepositConfirmed requestEvent);
    ResponsePendingEvent retrieveWithdrawPendingTx(WithdrawPending requestEvent);
    void retrieveWithdrawConfirmedTx(WithdrawConfirmed requestEvent);

}
