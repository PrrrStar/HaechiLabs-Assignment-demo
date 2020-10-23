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
    private final int depositId;

    private final String walletId;


    public DepositReorged() {
        this(0, null);
    }
}