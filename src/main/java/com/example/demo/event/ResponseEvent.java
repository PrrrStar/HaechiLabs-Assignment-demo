package com.example.demo.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode

public class ResponseEvent {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private Long id;
    //Request
    private final String txId;
    private final String txHash;
    private final String amount;
    private final String fromAddress;
    private final String toAddress;
    private final String ticker;


    public ResponseEvent() {
        this(null,null,null,null,null,null);
    }

}
