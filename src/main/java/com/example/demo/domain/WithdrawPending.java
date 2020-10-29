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
    private final String tx_id;

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
    @Column(name = "withdraw_id")
    private final int withdraw_id;


    public WithdrawPending() {
        this(null,null,null, null,null,null,-1);
    }
}