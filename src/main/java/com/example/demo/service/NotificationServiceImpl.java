package com.example.demo.service;

import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import com.example.demo.domain.WithdrawConfirmed;
import com.example.demo.domain.WithdrawPending;
import com.example.demo.event.RequestEvent;
import com.example.demo.event.ResponseDepositMinedEvent;
import com.example.demo.event.ResponseWithdrawPendingEvent;
import com.example.demo.repository.DepositMinedRepository;
import com.example.demo.repository.WithdrawPendingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private WithdrawPendingRepository withdrawPendingRepository;
    private DepositMinedRepository depositMinedRepository;

    NotificationServiceImpl(final DepositMinedRepository depositMinedRepository,
                            final WithdrawPendingRepository withdrawPendingRepository){
        this.depositMinedRepository = depositMinedRepository;
        this.withdrawPendingRepository = withdrawPendingRepository;
    }
    @Override
    public ResponseDepositMinedEvent retrieveDepositMinedTx(RequestEvent requestEvent) {
        System.out.println("\nRequest Deposit Mined"+ requestEvent);
        ResponseDepositMinedEvent response = new ResponseDepositMinedEvent();

        //Transaction Hash 로 MINED 상태 조회
        Optional<DepositMined> depositMinedTx = depositMinedRepository.findByTxHash(requestEvent.getTx_hash());
        if (depositMinedTx.isPresent()){
            System.out.println(depositMinedTx.get().getDepositId());
            response = new ResponseDepositMinedEvent(
                    depositMinedTx.get().getDepositId()
            );
            System.out.println("\nResponse : "+ response);
        }
        System.out.println("\nResponse : "+ response);
        return response;
    }

    @Override
    public void retrieveDepositReorgedTx(RequestEvent requestEvent){

    }


    @Override
    public void retrieveDepositConfirmedTx(RequestEvent requestEvent) {

    }

    @Override
    public ResponseWithdrawPendingEvent retrieveWithdrawPendingTx(RequestEvent requestEvent) {
        System.out.println("\nRequest Withdraw Pending"+ requestEvent);
        ResponseWithdrawPendingEvent response = new ResponseWithdrawPendingEvent();

        //Transaction Id 로 PENDING 상태 조회
        Optional<WithdrawPending> withdrawPendingTx = withdrawPendingRepository.findByTxId(requestEvent.getTx_id());
        if (withdrawPendingTx.isPresent()){
            System.out.println(withdrawPendingTx.get().getWithdrawId());
            response = new ResponseWithdrawPendingEvent(
                    withdrawPendingTx.get().getWithdrawId()
            );
        }
        System.out.println("\nResponse : "+ response);
        return response;
    }

    @Override
    public void retrieveWithdrawConfirmedTx(RequestEvent requestEvent) {
       // System.out.println("\nWithdraw Confirmed : "+ withdrawConfirmed);

    }


    public List<DepositMined> dm(DepositMined depositMined) {
        //System.out.println("\nDeposit Confirmed : "+ depositMined);
        return null;
    }


}
