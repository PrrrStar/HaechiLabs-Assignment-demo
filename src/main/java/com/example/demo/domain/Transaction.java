package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;


/**
 * (트랜잭션 상태, 입출금상태)와 트랜잭션을 연결하는 클래스
 */

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private final String txId;

    private final String txHash;
    private final String txStatus;
    private final String transferType;
    private final String fromAddress;
    private final String toAddress;
    private final String walletId;
    private final String ticker;
    private final String amount;



    // JSON/JPA 를 위한 빈 생성자
    public Transaction() {
        this(null, null, null, null, null, null, null, null, null);
    }

}
