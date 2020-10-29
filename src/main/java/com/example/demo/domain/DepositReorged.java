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
    private final int deposit_id;

    @Column(name = "wallet_id")
    private final String wallet_id;


    public DepositReorged() {
        this(-1, null);
    }
}