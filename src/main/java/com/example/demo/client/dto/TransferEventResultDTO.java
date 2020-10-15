package com.example.demo.client.dto;

import com.example.demo.client.TransferEventDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;


/**
 * 트랜잭션이 코인입출금 내역 에서 받아온 데이터를 정의한 클래스
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonDeserialize(using = TransferEventDeserializer.class)
public class TransferEventResultDTO {

    private final int id;
    private final String from;
    private final String to;
    private final String amount;
    private final String blockchain;
    private final String status;
    private final String confirmation;
    private final String walletId;
    private final String orgId;
    private final String masterWalletId;
    private final String transactionId;
    private final String coinSymbol;
    private final String transferType;
    private final String blockHash;
    private final String transactionHash;
    private final String createdAt;
    private final String updatedAt;
    private final String walletName;
    private final String walletType;

    // Json/JPA 를 위한 빈 생성자
    TransferEventResultDTO(){
        id = 1;
        from = null;
        to = null;
        amount = null;
        blockchain = null;
        status = null;
        confirmation = null;
        walletId = null;
        orgId = null;
        masterWalletId = null;
        transactionId = null;
        coinSymbol = null;
        transferType = null;
        blockHash = null;
        transactionHash = null;
        createdAt = null;
        updatedAt = null;
        walletName = null;
        walletType = null;

    }
}
