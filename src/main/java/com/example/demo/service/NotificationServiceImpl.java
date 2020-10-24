package com.example.demo.service;

import com.example.demo.domain.*;

import com.example.demo.event.ResponseMinedEvent;
import com.example.demo.event.ResponsePendingEvent;
import com.example.demo.repository.DepositMinedRepository;
import com.example.demo.repository.WithdrawPendingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


    /**
     * Transaction Hash 로 입금 MINED 상태를 조회합니다.
     * @param depositMined
     * @return deposit Id
     */
    @Override
    public ResponseMinedEvent retrieveDepositMinedTx(DepositMined depositMined) {
        ResponseMinedEvent response = new ResponseMinedEvent();

        //Transaction Hash 로 MINED 상태 조회
        Optional<DepositMined> depositMinedTx = depositMinedRepository.findByTxHash(depositMined.getTxHash());
        if (depositMinedTx.isPresent()){
            response = new ResponseMinedEvent(
                    depositMinedTx.get().getDepositId()
            );
            System.out.println("======================================");
            System.out.println("Response Deposit Mined");
            System.out.println(response);
            System.out.println("======================================");
        }
        return response;
    }

    @Override
    public void retrieveDepositReorgedTx(DepositReorged depositReorged){
        System.out.println("======================================");
        System.out.println("Deposit Reorged Information");
        System.out.println(depositReorged);
        System.out.println("======================================");

    }


    @Override
    public void retrieveDepositConfirmedTx(DepositConfirmed depositConfirmed) {
        System.out.println("======================================");
        System.out.println("Deposit Confirmed Information");
        System.out.println(depositConfirmed);
        System.out.println("======================================");
    }

    /**
     * Transaction Id 로 출금 PENDING 상태를 조회합니다.
     * @param withdrawPending
     * @return withdraw Id
     */
    @Override
    public ResponsePendingEvent retrieveWithdrawPendingTx(WithdrawPending withdrawPending) {
        ResponsePendingEvent response = new ResponsePendingEvent();

        Optional<WithdrawPending> withdrawPendingTx = withdrawPendingRepository.findByTxId(withdrawPending.getTxId());
        if (withdrawPendingTx.isPresent()){
            response = new ResponsePendingEvent(
                    withdrawPendingTx.get().getWithdrawId()
            );
            System.out.println("======================================");
            System.out.println("Response Withdraw Pending");
            System.out.println(response);
            System.out.println("======================================");
        }
        return response;
    }

    @Override
    public void retrieveWithdrawConfirmedTx(WithdrawConfirmed withdrawConfirmed) {
        System.out.println("======================================");
        System.out.println("Deposit Confirmed Information");
        System.out.println(withdrawConfirmed);
        System.out.println("======================================");

    }
}
