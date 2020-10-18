package com.example.demo.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode

public abstract class TransferResultEvent {


    @JsonProperty("walletType")
    private String wallettype;
    @JsonProperty("walletName")
    private String walletname;
    @JsonProperty("updatedAt")
    private String updatedat;
    @JsonProperty("createdAt")
    private String createdat;
    @JsonProperty("transactionHash")
    private String transactionhash;
    @JsonProperty("transferType")
    private String transfertype;
    @JsonProperty("blockHash")
    private String blockhash;
    @JsonProperty("coinSymbol")
    private String coinsymbol;
    @JsonProperty("transactionId")
    private String transactionid;
    @JsonProperty("masterWalletId")
    private String masterwalletid;
    @JsonProperty("orgId")
    private String orgid;
    @JsonProperty("walletId")
    private String walletid;
    @JsonProperty("confirmation")
    private String confirmation;
    @JsonProperty("status")
    private String status;
    @JsonProperty("blockchain")
    private String blockchain;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("to")
    private String to;
    @JsonProperty("from")
    private String from;
    @JsonProperty("id")
    private int id;
}
