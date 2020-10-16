package com.example.demo.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


/**
 * 트랜잭션이 코인입출금 내역 에서 받아온 데이터를 정의한 클래스
 */


@Data
public class TransferEventResultDTO {

    @JsonProperty("results")
    private List<TransferEventResultDTO.Results> results;
    @JsonProperty("pagination")
    private TransferEventResultDTO.Pagination pagination;

    @Data
    public static class Results {
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
        @JsonProperty("blockHash")
        private String blockhash;
        @JsonProperty("transferType")
        private String transfertype;
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

    @Data
    public static class Pagination {
        @JsonProperty("totalCount")
        private int totalcount;
        @JsonProperty("previousUrl")
        private String previousUrl;
        @JsonProperty("nextUrl")
        private String nexturl;
    }
}
