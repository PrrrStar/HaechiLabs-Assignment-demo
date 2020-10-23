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
    private final String txId;
    private final String txHash;
    private final int depositId;

    private final String walletId;


    public DepositConfirmed() {
        this(null,null, 0,null);
    }
}
