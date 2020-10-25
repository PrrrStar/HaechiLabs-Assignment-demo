package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public class DepositConfirmed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    //Request
    @Column(name = "tx_id")
    private final String txId;

    @Column(name = "tx_hash")
    private final String txHash;

    @Column(name = "deposit_id")
    private final int depositId;

    @Column(name = "wallet_id")
    private final String walletId;


    public DepositConfirmed() {
        this(null,null, -1,null);
    }
}
