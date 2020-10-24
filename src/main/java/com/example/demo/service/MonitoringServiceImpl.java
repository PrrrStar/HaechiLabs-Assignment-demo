package com.example.demo.service;

import com.example.demo.client.TransferEventClient;
import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.*;
import com.example.demo.event.EventDispatcher;
import com.example.demo.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MonitoringServiceImpl implements MonitoringService {

    private DepositMinedRepository depositMinedRepository;
    private DepositReorgedRepository depositReorgedRepository;
    private DepositConfirmedRepository depositConfirmedRepository;
    private WithdrawPendingRepository withdrawPendingRepository;
    private WithdrawConfirmedRepository withdrawConfirmedRepository;
    private TransferEventClient transferEventClient;
    private EventDispatcher eventDispatcher;

    MonitoringServiceImpl(DepositMinedRepository depositMinedRepository,
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
    public ResponseEntity<TransferEventResultDTO> retrieveAllTxInfo(String url)throws JsonProcessingException {
        ResponseEntity<TransferEventResultDTO> transferResults = transferEventClient.retrieveTransferResults(url);

        String nextURL = transferResults.getBody().getPagination().getNextUrl();
        List<TransferEventResultDTO.Results> results = transferResults.getBody().getResults();

        results.forEach(this::txDetector);

        /*
        if (nextURL == null){
            System.out.println("Exit");
            return transferResults;
        }else{
            retrieveAllTxInfo(nextURL);
            System.out.println(nextURL);
        }

         */
        return transferResults;

    }


    /**
     * 각 트랜잭션 별 Transfer Type 과 Status에 따라 해당 객체에 set 합니다.
     * 그 후 조건에 맞는 트랜잭션만 Entity 에 save 합니다.
     *
     * @param results
     */
    private void txDetector(final TransferEventResultDTO.Results results){

        int eventId = results.getId();
        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("MINED")){

            //Deposit Mined Logic
            if(!depositMinedRepository.findByDepositId(eventId).isPresent()) {
                saveDepositMinedByDepositId(results);
            }

            //Deposit Reorged Logic
            //Mined 됐는데 Pending 에도 존재하고(같은 ID가 둘다 있다는 것은 MINED > PENDING 으로 바뀌었다.)
            //Reorged 에 해당 ID가 없을때 저장한다.
            if(depositMinedRepository.findByDepositId(eventId).isPresent()
                    &&withdrawPendingRepository.findByWithdrawId(eventId).isPresent()
                    &&!depositReorgedRepository.findByDepositId(eventId).isPresent()){
                saveDepositReorgedByDepositId(results);
            }

            //eventDispatcher.depositMindedSend(depositMined, "notification_exchange","queue.depositMined");
        }

        //Deposit Confirmed Logic
        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("CONFIRMED")){
            saveDepositConfirmedByDepositId(results, eventId);

        }

        //Withdraw Pending Logic
        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("PENDING")){
            saveWithdrawPendingByWithdrawId(results, eventId);

        }

        //Withdraw Confirmed Logic
        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("CONFIRMED")){
            saveWithdrawConfirmedByWithdrawId(results, eventId);

        }

    }

    private DepositMined saveDepositMinedByDepositId(final TransferEventResultDTO.Results results){
        DepositMined depositMined = new DepositMined(
                results.getTransactionHash(),
                results.getAmount(),
                results.getFrom(),
                results.getTo(),
                results.getWalletId(),
                results.getCoinSymbol(),
                results.getId()
        );
        System.out.println("Save Deposit Mined transaction... ");
        depositMinedRepository.save(depositMined);
        System.out.println("Success...!");
        return depositMined;
    }


    private DepositReorged saveDepositReorgedByDepositId(final TransferEventResultDTO.Results results){
        DepositReorged depositReorged = new DepositReorged(
                results.getId(),
                results.getWalletId()
        );
        System.out.println("Save Deposit Reorged transaction... ");
        depositReorgedRepository.save(depositReorged);
        System.out.println("Success...!");
        return depositReorged;
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
            //System.out.println("Save Deposit Confirmed transaction... ");
            depositConfirmedRepository.save(depositConfirmed);
            //System.out.println("Success...!");
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
            //System.out.println("Save Withdraw Confirmed transaction... ");
            withdrawConfirmedRepository.save(withdrawConfirmed);
           // System.out.println("Success...!");

        }
        return withdrawConfirmed;
    }


}
