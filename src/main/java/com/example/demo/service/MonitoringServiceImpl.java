package com.example.demo.service;

import com.example.demo.client.TransferEventClient;
import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.*;
import com.example.demo.event.EventDispatcher;
import com.example.demo.repository.*;

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
     * 전체 트랜잭션 정보 모니터링 조회
     * @return List<TransferEventResultDTO.Results>
     */

    @Override
    public List<TransferEventResultDTO.Results> retrieveTransactionInfo(String url, String size, int page, String updatedAtGte){
        ResponseEntity<TransferEventResultDTO> transferResults = transferEventClient.detectTransferEvent(url, size, Long.toString(page), updatedAtGte);

        String nextURL = transferResults.getBody().getPagination().getNextUrl();
        List<TransferEventResultDTO.Results> results = transferResults.getBody().getResults();

        // txDetector 는 각 Transaction status, transfer type 을 감지하고 Entity에 save 한다.
        results.forEach(this::txDetector);


        // 다음 페이지가 없으면 Recursive 탈출
        if (nextURL == null){
            return results;
        }
        else {
            retrieveTransactionInfo(url, size, page+1, updatedAtGte);
        }
        return results;
    }


    /**
     * 각 트랜잭션 별 Transfer Type 과 Status에 따라 해당 객체에 set 합니다.
     * 그 후 조건에 맞는 트랜잭션만 Entity 에 save 합니다.
     *
     * @param results
     */
    private void txDetector(final TransferEventResultDTO.Results results){

        int eventId = results.getId();

        //Deposit Mined Logic
        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("MINED")){
            // id 가 존재하지 않으면 저장한다.
            if(!depositMinedRepository.findByDepositId(eventId).isPresent()) {
                DepositMined request = saveDepositMinedByDepositId(results);

                // 저장한 값을 depositMined Queue 로 보낸다.
                eventDispatcher.depositMindedSend(request, "notification_exchange","queue.depositMined");
            }
        }

        //Deposit Reorged Logic
        //Mined 됐는데 Pending 에도 존재하고(같은 ID가 둘다 있다는 것은 MINED > PENDING 으로 바뀌었다.)
        //Reorged 에 해당 ID가 없을때 저장한다.
        if(depositMinedRepository.findByDepositId(eventId).isPresent()
                &&withdrawPendingRepository.findByWithdrawId(eventId).isPresent()
                &&!depositReorgedRepository.findByDepositId(eventId).isPresent()){
            DepositReorged request = saveDepositReorgedByDepositId(results);
            eventDispatcher.depositReorgedSend(request, "notification_exchange","queue.depositReorged");
        }

        //Deposit Confirmed Logic
        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("CONFIRMED")){
            if(!depositConfirmedRepository.findByDepositId(eventId).isPresent()){
                DepositConfirmed request = saveDepositConfirmedByDepositId(results);
                eventDispatcher.depositConfirmedSend(request, "notification_exchange","queue.depositConfirmed");
            }
        }

        //Withdraw Pending Logic
        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("PENDING")){
            if(!withdrawPendingRepository.findByWithdrawId(eventId).isPresent()) {
                WithdrawPending request = saveWithdrawPendingByWithdrawId(results);
                eventDispatcher.withdrawPendingSend(request, "notification_exchange","queue.withdrawPending");
            }

        }

        //Withdraw Confirmed Logic
        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("CONFIRMED")){
            if(!withdrawConfirmedRepository.findByWithdrawId(eventId).isPresent()) {
                WithdrawConfirmed request = saveWithdrawConfirmedByWithdrawId(results);
                eventDispatcher.withdrawConfirmedSend(request, "notification_exchange","queue.withdrawConfirmed");
            }
        }

    }


    /**
     * 채굴된 트랜잭션 정보를 저장하는 메서드
     * @param results
     * @return 저장된 객체 정보를 return 합니다.
     */
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
        System.out.println("==================================");
        System.out.println("Save Deposit Mined transaction... ");
        depositMinedRepository.save(depositMined);
        System.out.println("Success...!");
        System.out.println("==================================");
        return depositMined;
    }


    private DepositReorged saveDepositReorgedByDepositId(final TransferEventResultDTO.Results results){
        DepositReorged depositReorged = new DepositReorged(
                results.getId(),
                results.getWalletId()
        );
        System.out.println("====================================");
        System.out.println("Save Deposit Reorged transaction... ");
        depositReorgedRepository.save(depositReorged);
        System.out.println("Success...!");
        System.out.println("====================================");
        return depositReorged;
    }

    private DepositConfirmed saveDepositConfirmedByDepositId(final TransferEventResultDTO.Results results){
        DepositConfirmed depositConfirmed = new DepositConfirmed(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getId(),
                results.getWalletId()
        );
        System.out.println("======================================");
        System.out.println("Save Deposit Confirmed transaction... ");
        depositConfirmedRepository.save(depositConfirmed);
        System.out.println("Success...!");
        System.out.println("======================================");

        return depositConfirmed;
    }


    private WithdrawPending saveWithdrawPendingByWithdrawId(final TransferEventResultDTO.Results results){
        WithdrawPending withdrawPending = new WithdrawPending(
                results.getTransactionId(),
                results.getAmount(),
                results.getFrom(),
                results.getTo(),
                results.getWalletId(),
                results.getCoinSymbol(),
                results.getId()
        );
        System.out.println("=====================================");
        System.out.println("Save Withdraw Pending transaction... ");
        withdrawPendingRepository.save(withdrawPending);
        System.out.println("Success...!");
        System.out.println("======================================");
        return withdrawPending;
    }



    private WithdrawConfirmed saveWithdrawConfirmedByWithdrawId(final TransferEventResultDTO.Results results){
         WithdrawConfirmed withdrawConfirmed = new WithdrawConfirmed(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getId(),
                results.getWalletId()
        );
        System.out.println("=======================================");
        System.out.println("Save Withdraw Confirmed transaction... ");
        withdrawConfirmedRepository.save(withdrawConfirmed);
        System.out.println("Success...!");
        System.out.println("=======================================");

        return withdrawConfirmed;
    }


}
