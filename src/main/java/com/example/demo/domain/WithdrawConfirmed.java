package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public class WithdrawConfirmed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    //Request
    @Column(name = "tx_id")
    private final String txId;

    @Column(name = "tx_hash")
    private final String txHash;

    @Column(name = "withdraw_id")
    private final int withdrawId;

    @Column(name = "wallet_id")
    private final String walletId;



    public WithdrawConfirmed() {
        this(null,null,-1, null);
    }
}