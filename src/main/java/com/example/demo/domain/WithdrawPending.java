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
    @Column(name = "tx_id")
    private final String txId;

    @Column(name = "amount")
    private final String amount;

    @Column(name = "from_address")
    private final String fromAddress;

    @Column(name = "to_address")
    private final String toAddress;

    @Column(name = "wallet_id")
    private final String walletId;

    @Column(name = "ticker")
    private final String ticker;

    //Response
    @Column(name = "withdraw_id")
    private final int withdrawId;


    public WithdrawPending() {
        this(null,null,null, null,null,null,-1);
    }
}