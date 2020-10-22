package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;


/**
 * (트랜잭션 상태, 입출금상태)와 트랜잭션을 연결하는 클래스
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public final class Notification {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private Long id;
    //Request
    private final String txId;
    private final String txHash;
    private final String amount;
    private final String fromAddress;
    private final String toAddress;
    private final String ticker;


    public Notification() {
        this(null,null,null,null,null,null);
    }
}
