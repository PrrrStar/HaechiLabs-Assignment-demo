package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public class DepositReorged {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    //Request
    @Column(name = "deposit_id")
    private final int depositId;

    @Column(name = "wallet_id")
    private final String walletId;


    public DepositReorged() {
        this(-1, null);
    }
}