package com.example.demo.service;

import com.example.demo.client.TransferEventClient;
import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.*;
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
        WithdrawPending withdrawPending;
        WithdrawConfirmed withdrawConfirmed;

        int eventId = results.getId();
        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("MINED")){
            depositMined = saveDepositMinedByDepositId(results, eventId);
            eventDispatcher.depositMindedSend(depositMined, "notification_exchange","queue.depositMined");
        }

        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("CONFIRMED")){
            depositConfirmed = saveDepositConfirmedByDepositId(results, eventId);
            eventDispatcher.depositConfirmedSend(depositConfirmed, "notification_exchange","queue.depositConfirmed");
        }
        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("PENDING")){
            withdrawPending = saveWithdrawPendingByWithdrawId(results, eventId);
            eventDispatcher.withdrawPendingSend(withdrawPending, "notification_exchange","queue.withdrawPending");
        }
        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("CONFIRMED")){
            withdrawConfirmed = saveWithdrawConfirmedByWithdrawId(results, eventId);
            eventDispatcher.withdrawConfirmedSend(withdrawConfirmed, "notification_exchange","queue.withdrawConfirmed");
        }

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
            System.out.println("Save Deposit Mined transaction... ");
            depositMinedRepository.save(depositMined);
            System.out.println("Success...!");
        }
        return depositMined;
    }

    private DepositConfirmed saveDepositConfirmedByDepositId(final TransferEventResultDTO.Results results,
                                                            final int depositId){
        DepositConfirmed depositConfirmed = new DepositConfirmed(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getId(),
                results.getWalletId()
        );
        if(!depositConfirmedRepository.findByDepositId(depositId).isPresent()){
            System.out.println("Save Deposit Confirmed transaction... ");
            depositConfirmedRepository.save(depositConfirmed);
            System.out.println("Success...!");
        }
        return depositConfirmed;
    }


    private WithdrawPending saveWithdrawPendingByWithdrawId(final TransferEventResultDTO.Results results,
                                                          final int withdrawId){
        WithdrawPending withdrawPending = new WithdrawPending(
                results.getTransactionId(),
                results.getAmount(),
                results.getFrom(),
                results.getTo(),
                results.getWalletId(),
                results.getCoinSymbol(),
                results.getId()
        );
        if(!withdrawPendingRepository.findByWithdrawId(withdrawId).isPresent()){
            System.out.println("Save Withdraw Pending transaction... ");
            withdrawPendingRepository.save(withdrawPending);
            System.out.println("Success...!");
        }
        return withdrawPending;
    }



    private WithdrawConfirmed saveWithdrawConfirmedByWithdrawId(final TransferEventResultDTO.Results results,
                                                            final int withdrawId){
        WithdrawConfirmed withdrawConfirmed = new WithdrawConfirmed(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getId(),
                results.getWalletId()
        );
        if(!withdrawConfirmedRepository.findByWithdrawId(withdrawId).isPresent()){
            System.out.println("Save Withdraw Confirmed transaction... ");
            withdrawConfirmedRepository.save(withdrawConfirmed);
            System.out.println("Success...!");

        }
        return withdrawConfirmed;
    }



    @Override
    public List<DepositMined> retrieveDepositMinedTx(DepositMined depositMined) {
        System.out.println("\nDeposit Mined!"+ depositMined);
        return null;
    }

    @Override
    public List<DepositConfirmed> retrieveDepositConfirmedTx(DepositConfirmed depositConfirmed) {
        System.out.println("\nDeposit Confirmed : "+ depositConfirmed);
        return null;
    }

    @Override
    public List<WithdrawPending> retrieveWithdrawPendingTx(WithdrawPending withdrawPending) {
        System.out.println("\nWithdraw Pending : "+ withdrawPending);
        return null;
    }

    @Override
    public List<WithdrawConfirmed> retrieveWithdrawConfirmedTx(WithdrawConfirmed withdrawConfirmed) {
        System.out.println("\nWithdraw Confirmed : "+ withdrawConfirmed);
        return null;
    }


}
