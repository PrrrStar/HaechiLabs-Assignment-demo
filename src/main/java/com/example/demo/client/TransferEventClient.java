package com.example.demo.client;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

/**
 * 코인 입출금 내역과 연결하는 인터페이스
 */
public interface TransferEventClient {


    ResponseEntity<TransferEventResultDTO> retrieveTransferResults(String url) throws JsonProcessingException;
    ResponseEntity<TransferEventResultDTO> detectTransferEvent(String url,
                                                               String size,
                                                               String page,
                                                               String status,
                                                               String walletId,
                                                               String masterWalletId,
                                                               String updatedAtGte);
}
