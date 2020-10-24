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
    private final String txId;
    private final String txHash;
    private final int withdrawId;

    private final String walletId;



    public WithdrawConfirmed() {
        this(null,null,-1, null);
    }
}