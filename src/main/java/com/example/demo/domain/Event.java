package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private final String from;
    private final String to;
    private final String amount;
    private final String masterWalletId;
    private final String transactionHash;
    private final String coinSymbol;

    private final String status;
    private final String transferType;
    private final String orgId;
}
