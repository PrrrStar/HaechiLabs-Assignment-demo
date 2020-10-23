package com.example.demo.service;

import com.example.demo.client.TransferEventClient;
import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositMined;
import com.example.demo.domain.DepositReorged;
import com.example.demo.event.EventDispatcher;
import com.example.demo.event.ResponseEvent;
import com.example.demo.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private DepositMinedRepository depositMinedRepository;
    private DepositReorgedRepository depositReorgedRepository;
    private DepositConfirmedRepository depositConfirmedRepository;
    private WithdrawPendingRepository withdrawPendingRepository;
    private WithdrawConfirmedRepository withdrawConfirmedRepository;
    private TransferEventClient transferEventClient;
    private EventDispatcher eventDispatcher;

    NotificationServiceImpl(DepositMinedRepository depositMinedRepository,
                            DepositReorgedRepository depositReorgedRepository,
                            DepositConfirmedRepository depositConfirmedRepository,
                            WithdrawPendingRepository withdrawPendingRepository,
                            WithdrawConfirmedRepository withdrawConfirmedRepository,
                            TransferEventClient transferEventClient,
                            EventDispatcher eventDispatcher){
        this.depositMinedRepository = depositMinedRepository;
        this.depositReorgedRepository = depositReorgedRepository;
        this.depositConfirmedRepository = depositConfirmedRepository;
        this.withdrawPendingRepository = withdrawPendingRepository;
        this.withdrawConfirmedRepository = withdrawConfirmedRepository;

        this.transferEventClient = transferEventClient;
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * 전체 트랜잭션 정보 조회
     * @return List<TransferEventResultDTO.Results>
     */
    @Override
    public List<TransferEventResultDTO.Results> retrieveAllTxInfo()throws JsonProcessingException {
        List<TransferEventResultDTO.Results> results = transferEventClient.retrieveTransferEventResultDTO().getResults();
        results.stream().forEach(x->{
            txDetector(x);
            System.out.println(x);
        });
        return results;
    }


    /**
     * 전체 트랜잭션 중 Transfer Type 과 Status 별로 DepositMined 객체에 set 한다.
     * 해당 객체는 Rabbit MQ의 send Method 로 각각의 큐에 보내진다.
     * @param results
     * @return
     */
    private void txDetector(final TransferEventResultDTO.Results results){
        DepositMined depositMined;
        DepositConfirmed depositConfirmed;
        DepositReorged depositReorged;

        int eventId = results.getId();
        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("MINED")){
            depositMined = saveDepositMinedByDepositId(results, eventId);
            eventDispatcher.depositMindedSend(depositMined, "notification_exchange","queue.depositMined");
        }

        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("CONFIRMED")){
            depositConfirmed = saveDepositConfirmedByEventId(results, eventId);
            eventDispatcher.depositConfirmedSend(depositConfirmed, "notification_exchange","queue.depositConfirmed");
        }
//        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("PENDING")){
//            depositMined = setNotificationInfo(results);
//            eventDispatcher.send(depositMined, "notification_exchange","queue.withdrawPending");
//        }
//        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("CONFIRMED")){
//            depositMined = setNotificationInfo(results);
//            eventDispatcher.send(depositMined, "notification_exchange","queue.withdrawConfirmed");
//        }

    }

    private DepositMined saveDepositMinedByDepositId(final TransferEventResultDTO.Results results,
                                            final int depositId){
        DepositMined depositMined = new DepositMined(
                results.getTransactionHash(),
                results.getAmount(),
                results.getFrom(),
                results.getTo(),
                results.getWalletId(),
                results.getCoinSymbol(),
                results.getId()
        );
        if(!depositMinedRepository.findByDepositId(depositId).isPresent()){
            depositMinedRepository.save(depositMined);
            System.out.println("입금 채굴 상태 저장");
        }
        return depositMined;
    }

    private DepositConfirmed saveDepositConfirmedByEventId(final TransferEventResultDTO.Results results,
                                                            final int depositId){
        DepositConfirmed depositConfirmed = new DepositConfirmed(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getId(),
                results.getWalletId()
        );
        if(!depositConfirmedRepository.findByDepositId(depositId).isPresent()){
            depositConfirmedRepository.save(depositConfirmed);
            System.out.println("입금 확인 상태 저장");
        }
        return depositConfirmed;
    }


    @Override
    public List<DepositMined> retrieveDepositMinedTx(DepositMined depositMined) {
        System.out.println("Deposit Mined : "+ depositMined);

        return null;
    }

    @Override
    public List<DepositConfirmed> retrieveDepositConfirmedTx(DepositConfirmed depositConfirmed) {
        System.out.println("Deposit Confirmed : "+ depositConfirmed);
        return null;
    }

    @Override
    public List<DepositMined> retrieveWithdrawPendingTx(DepositMined depositMined) {
        System.out.println("Withdraw Pending : "+ depositMined);
        return null;
    }

    @Override
    public List<DepositMined> retrieveWithdrawConfirmedTx(DepositMined depositMined) {
        System.out.println("Withdraw Confirmed : "+ depositMined);
        return null;
    }


}
