package com.example.demo.service;

import com.example.demo.client.TransferEventClient;
import com.example.demo.client.dto.TransferEventResultDTO;
import com.example.demo.domain.Notification;
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

    NotificationServiceImpl(NotificationRepository notificationRepository,
                            TransferEventClient transferEventClient){
        this.notificationRepository = notificationRepository;
        this.transferEventClient = transferEventClient;
    }


    /**
     * 입출금 타입과 트랜잭션 상태로 필터링
     * @param transferType
     * @param status
     * @return
     * @throws JsonProcessingException
     */

    @Override
    public List<TransferEventResultDTO.Results> retrieveTxByTransferTypeAndStatus(String transferType, String status)
            throws JsonProcessingException {

        List<TransferEventResultDTO.Results> results = transferEventClient.retrieveTransferEventResultDTO().getResults();
        List<TransferEventResultDTO.Results> transactions = results.stream()
                .filter(t -> t.getTransferType().contains(transferType))
                .filter(s -> s.getStatus().contains(status))
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        //List<TransferResultEvent> a = mapper.readValue(transactions.get(0).getClass(),TransferResultEvent.class);

        /*
         * 1초마다 Scheduler 로 컨트롤러에서 이 메서드를 호출하는건 굉장한 낭비다.
         * return 하기 전에 event 메세지를 보내는 건?
         *         System.out.println(transferType+" | "+status);
                    System.out.println(transactions);
                    System.out.println("");
         */

        return transactions;
    }

    /**
     * 입출금 타입으로 필터링
     * @param transferType
     * @return
     */
    @Override
    public List<TransferEventResultDTO.Results> retrieveTxByTransferType(String transferType)
            throws JsonProcessingException {

        List<TransferEventResultDTO.Results> results = transferEventClient.retrieveTransferEventResultDTO().getResults();
        List<TransferEventResultDTO.Results> transactions = results.stream()
                .filter(t -> t.getTransferType().contains(transferType))
                .collect(Collectors.toList());

        return transactions;
    }

    /**
     * 전체 트랜잭션 조회
     * @return
     */
    @Override
    public List<TransferEventResultDTO.Results> retrieveTxALL()throws JsonProcessingException {

        List<TransferEventResultDTO.Results> results = transferEventClient.retrieveTransferEventResultDTO().getResults();

        List<TransferEventResultDTO.Results> transactions = results.stream()
                .filter(t -> t.getTransferType().contains("DEPOSIT") && t.getStatus().contains("CONFIRMED"))
                .collect(Collectors.toList());
        transactions.stream().forEach(x->{
            saveTransactionInfo(x);
        });


        return transactions;
    }


    private Notification saveTransactionInfo(final TransferEventResultDTO.Results results){
        Notification notification = new Notification(
                results.getTransactionId(),
                results.getTransactionHash(),
                results.getAmount(),
                results.getFrom(),
                results.getTo(),
                results.getCoinSymbol()
        );
        System.out.println(notification);
        notificationRepository.save(notification);
        return notification;
    }

}
