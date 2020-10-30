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
    public List<TransferEventResultDTO.Results> retrieveTransactionInfo(String url,
                                                                        String size,
                                                                        int page,
                                                                        String status,
                                                                        String walletId,
                                                                        String masterWalletId,
                                                                        String updatedAtGte){

        ResponseEntity<TransferEventResultDTO> transferResults = transferEventClient.detectTransferEvent(url, size, Long.toString(page), status, walletId, masterWalletId, updatedAtGte);

        String nextURL = transferResults.getBody().getPagination().getNextUrl();
        List<TransferEventResultDTO.Results> results = transferResults.getBody().getResults();

        // txDetector 는 각 Transaction status, transfer type 을 감지하고 Entity 에 save 한다.
        results.forEach(this::txDetector);

        // 다음 페이지가 없으면 Recursive 탈출
        if (nextURL == null){
            return results;
        }else {
            // page+1 을 한 뒤 URI 요청을 보냅니다.
            retrieveTransactionInfo(url, size, page+1, status, walletId, masterWalletId,updatedAtGte);
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

        // Deposit Logic
        if (results.getTransferType().contains("DEPOSIT")){

            // Deposit Mined Logic
            // Deposit 에서 Transaction Status 가 MINED 인 경우,
            if (results.getStatus().contains("MINED")){
                // id 가 존재하지 않으면 저장한다.
                if(depositMinedRepository.getDepositMinedByDeposit_id(eventId).isEmpty()) {
                    DepositMined request = saveDepositMinedByDepositId(results);

                    // 저장한 객체를 depositMined Queue 로 보낸다.
                    eventDispatcher.depositMindedSend(request, "notification_exchange","queue.depositMined");
                }
            }

            // Deposit Reorged Logic
            // Deposit 에서 Transaction Status 가 PENDING or REPLACED 인 경우,
            if (results.getStatus().contains("PENDING")||results.getStatus().contains("REPLACED")) {
                // MINED 에 해당 ID 가 있고 Reorged 에는 없을 경우 저장한다.
                if (depositMinedRepository.getDepositMinedByDeposit_id(eventId).isPresent()
                        && depositReorgedRepository.getDepositReorgedByDeposit_id(eventId).isEmpty()) {
                    DepositReorged request = saveDepositReorgedByDepositId(results);
                    eventDispatcher.depositReorgedSend(request, "notification_exchange", "queue.depositReorged");
                }
            }

            // Deposit Confirmed Logic
            // Deposit 에서 Transaction Status 가 CONFIRMED 인 경우,
            if (results.getStatus().contains("CONFIRMED")){
                if(depositConfirmedRepository.getDepositConfirmedByDeposit_id(eventId).isEmpty()){
                    DepositConfirmed request = saveDepositConfirmedByDepositId(results);
                    eventDispatcher.depositConfirmedSend(request, "notification_exchange","queue.depositConfirmed");
                }
            }
        }


        // Withdraw Logic
        if (results.getTransferType().contains("WITHDRAWAL")){

            // Withdraw Pending Logic
            // Withdraw 에서 Transaction Status 가 PENDING 인 경우,
            if (results.getStatus().contains("PENDING")){
                if(!withdrawPendingRepository.getWithdrawPendingByWithdraw_id(eventId).isPresent()) {
                    WithdrawPending request = saveWithdrawPendingByWithdrawId(results);
                    eventDispatcher.withdrawPendingSend(request, "notification_exchange","queue.withdrawPending");
                }
            }

            // Withdraw Confirmed Logic
            // Withdraw 에서 Transaction Status 가 CONFIRMED 인 경우,
            if (results.getStatus().contains("CONFIRMED")){
                if(!withdrawConfirmedRepository.getWithdrawConfirmedByWithdraw_id(eventId).isPresent()) {
                    WithdrawConfirmed request = saveWithdrawConfirmedByWithdrawId(results);
                    eventDispatcher.withdrawConfirmedSend(request, "notification_exchange","queue.withdrawConfirmed");
                }
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
        System.out.println("\n==================================");
        System.out.println("Save Deposit Mined transaction... ");
        depositMinedRepository.save(depositMined);
        System.out.println("Success...!");
        System.out.println("==================================\n");
        return depositMined;
    }


    private DepositReorged saveDepositReorgedByDepositId(final TransferEventResultDTO.Results results){
        DepositReorged depositReorged = new DepositReorged(
                results.getId(),
                results.getWalletId()
        );
        System.out.println("\n====================================");
        System.out.println("Save Deposit Reorged transaction... ");
        depositReorgedRepository.save(depositReorged);
        System.out.println("Success...!");
        System.out.println("====================================\n");
        return depositReorged;
    }

    private DepositConfirmed saveDepositConfirmedByDepositId(final TransferEventResultDTO.Results results){
        DepositConfirmed depositConfirmed = new DepositConfirmed(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getId(),
                results.getWalletId()
        );
        System.out.println("\n======================================");
        System.out.println("Save Deposit Confirmed transaction... ");
        depositConfirmedRepository.save(depositConfirmed);
        System.out.println("Success...!");
        System.out.println("======================================\n");

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
        System.out.println("\n=====================================");
        System.out.println("Save Withdraw Pending transaction... ");
        withdrawPendingRepository.save(withdrawPending);
        System.out.println("Success...!");
        System.out.println("======================================\n");
        return withdrawPending;
    }



    private WithdrawConfirmed saveWithdrawConfirmedByWithdrawId(final TransferEventResultDTO.Results results){
         WithdrawConfirmed withdrawConfirmed = new WithdrawConfirmed(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getId(),
                results.getWalletId()
        );
        System.out.println("\n=======================================");
        System.out.println("Save Withdraw Confirmed transaction... ");
        withdrawConfirmedRepository.save(withdrawConfirmed);
        System.out.println("Success...!");
        System.out.println("=======================================\n");

        return withdrawConfirmed;
    }


}
