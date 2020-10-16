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
    private final String tx_id;

    private final String tx_hash;
    private final String from_adderss;
    private final String to_address;
    private final String wallet_id;
    private final String ticker;
    private final String amount;

    private final String status;
    private final String transferType;

    // JSON/JPA 를 위한 빈 생성자
    public Transaction() {
        this(null, null, null, null, null, null, null, null, null);
    }

}
