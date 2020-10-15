package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Event {
    private final String transactionId;
    private final String transactionHash;

    private final String from;
    private final String to;
    private final String masterWalletId;

    private final String coinSymbol;
    private final String amount;

    private final String status;
    private final String transferType;
    private final String orgId;

    public static Event emptyEvent(){
        return new Event(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
