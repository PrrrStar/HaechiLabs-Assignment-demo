package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
//@Table(name = "depositMinded", schema ="depositMinded")
public class DepositMinded {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private final Long deposit_id;

    @Column()
    private final String transferType;

    @Column()
    private final String status;


    public DepositMinded(){
        this(null, null, null);
    }
}
