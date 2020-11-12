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
    private String tx_id;

    @Column(name = "tx_hash")
    private final String tx_hash;

    @Column(name = "deposit_id")
    private final int deposit_id;

    @Column(name = "wallet_id")
    private final String wallet_id;


    public DepositConfirmed() {
        this(null,null, -1,null);
    }
}
