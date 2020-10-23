package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public class WithdrawPending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    //Request
    private final String txId;
    private final String amount;
    private final String fromAddress;
    private final String toAddress;
    private final String walletId;
    private final String ticker;

    //Response
    private final int withdrawId;


    public WithdrawPending() {
        this(null,null,null, null,null,null,0);
    }
}