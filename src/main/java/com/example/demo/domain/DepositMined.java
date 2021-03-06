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
public final class DepositMined {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private Long id;

    //Request
    @Column(name = "tx_hash")
    private final String tx_hash;

    @Column(name = "amount")
    private final String amount;

    @Column(name = "from_address")
    private final String from_address;

    @Column(name = "to_address")
    private final String to_address;

    @Column(name = "wallet_id")
    private final String wallet_id;

    @Column(name = "ticker")
    private final String ticker;


    //Response
    @Column(name = "deposit_id")
    private final int deposit_id;


    public DepositMined() {
        this(null,null,null,null,null,null,-1);
    }
}
