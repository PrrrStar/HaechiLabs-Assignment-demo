package com.example.demo.service;

import com.example.demo.client.TransferEventClient;
import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.Notification;
import com.example.demo.event.EventDispatcher;
import com.example.demo.event.EventHandler;
import com.example.demo.repository.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private NotificationRepository notificationRepository;
    private TransferEventClient transferEventClient;
    private EventDispatcher eventDispatcher;

    NotificationServiceImpl(NotificationRepository notificationRepository,
                            TransferEventClient transferEventClient,
                            EventDispatcher eventDispatcher){
        this.notificationRepository = notificationRepository;
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
     * 전체 트랜잭션 중 Transfer Type 과 Status 별로 Notification 객체에 set 한다.
     * 해당 객체는 Rabbit MQ의 send Method 로 각각의 큐에 보내진다.
     * @param results
     * @return
     */
    private Notification txDetector(final TransferEventResultDTO.Results results){
        Notification notification = new Notification();
        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("MINED")){
            notification = setNotificationInfo(results);
            eventDispatcher.send(notification, "notification_exchange","queue.depositMined");
        }
        if (results.getTransferType().contains("DEPOSIT") && results.getStatus().contains("CONFIRMED")){
            notification = setNotificationInfo(results);
            eventDispatcher.send(notification, "notification_exchange","queue.depositConfirmed");
        }

        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("PENDING")){
            notification = setNotificationInfo(results);
            eventDispatcher.send(notification, "notification_exchange","queue.withdrawPending");
        }
        if (results.getTransferType().contains("WITHDRAWAL") && results.getStatus().contains("CONFIRMED")){
            notification = setNotificationInfo(results);
            eventDispatcher.send(notification, "notification_exchange","queue.withdrawConfirmed");
        }

        return notification;
    }

    private void saveNotification(final TransferEventResultDTO.Results results){
        Notification notification = setNotificationInfo(results);
        notificationRepository.save(notification);
    }

    /**
     * 타입, 상태 별 객체를 할당하는 Method
     * @param results
     * @return Notification
     */
    private Notification setNotificationInfo(final TransferEventResultDTO.Results results){
        Notification notification = new Notification(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getAmount(),
                results.getFrom(),
                results.getTo(),
                results.getCoinSymbol()
        );
        return notification;
    }

    @Override
    public List<TransferEventResultDTO.Results> retrieveDepositMinedTx(Notification notification) {
        System.out.println("Deposit Mined : "+notification);
        return null;
    }

    @Override
    public List<TransferEventResultDTO.Results> retrieveDepositConfirmedTx(Notification notification) {
        System.out.println("Deposit Confirmed : "+notification);
        return null;
    }

    @Override
    public List<TransferEventResultDTO.Results> retrieveWithdrawPendingTx(Notification notification) {
        System.out.println("Withdraw Pending : "+notification);
        return null;
    }

    @Override
    public List<TransferEventResultDTO.Results> retrieveWithdrawConfirmedTx(Notification notification) {
        System.out.println("Withdraw Confirmed : "+notification);
        return null;
    }


}
