package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionStatus {
    private List<String> from;
    private List<String> to;
    private List<String> amount;
    private List<String> masterWalletId;
    private List<String> transactionHash;
    private List<String> coinSymbol;

    private List<String> status;
    private List<String> transferType;
    private List<String> orgId;
}
