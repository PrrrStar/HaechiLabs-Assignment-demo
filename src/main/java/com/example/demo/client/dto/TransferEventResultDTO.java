package com.example.demo.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


/**
 * 트랜잭션이 코인입출금 내역 에서 받아온 데이터를 정의한 DTO클래스
 * 계층형 JSON 구조이기 때문에 Nested class 로 구성했습니다.
 */


@Data
public class TransferEventResultDTO {


    @JsonProperty("pagination")
    private TransferEventResultDTO.Pagination pagination;
    @JsonProperty("results")
    private List<TransferEventResultDTO.Results> results;

    @Data
    public static class Results {
        @JsonProperty("id")
        private int id;
        @JsonProperty("from")
        private String from;
        @JsonProperty("to")
        private String to;
        @JsonProperty("amount")
        private String amount;
        @JsonProperty("decimals")
        private String decimals;
        @JsonProperty("blockchain")
        private String blockchain;
        @JsonProperty("status")
        private String status;
        @JsonProperty("confirmation")
        private String confirmation;
        @JsonProperty("walletId")
        private String walletId;
        @JsonProperty("orgId")
        private String orgid;
        @JsonProperty("masterWalletId")
        private String masterWalletId;
        @JsonProperty("transactionId")
        private String transactionId;
        @JsonProperty("coinSymbol")
        private String coinSymbol;
        @JsonProperty("blockHash")
        private String blockHash;
        @JsonProperty("transferType")
        private String transferType;
        @JsonProperty("transactionHash")
        private String transactionHash;
        @JsonProperty("createdAt")
        private String createdAt;
        @JsonProperty("updatedAt")
        private String updatedAt;
        @JsonProperty("walletName")
        private String walletName;
        @JsonProperty("walletType")
        private String walletType;
    }

    @Data
    public static class Pagination {
        @JsonProperty("nextUrl")
        private String nextUrl;
        @JsonProperty("previousUrl")
        private String previousUrl;
        @JsonProperty("totalCount")
        private int totalCount;
    }
}
