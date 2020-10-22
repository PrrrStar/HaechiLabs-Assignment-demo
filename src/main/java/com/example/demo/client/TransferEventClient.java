package com.example.demo.client;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

/**
 * 코인 입출금 내역과 연결하는 인터페이스
 */
public interface TransferEventClient {

    //Object call 시간이 어느 것이 짧을까..?

    //DTO 객체로 return 하는 매서드
    TransferEventResultDTO retrieveTransferEventResultDTO() throws JsonProcessingException;

    //Entity 로 return 하는 메서드
    ResponseEntity<TransferEventResultDTO.Results> retrieveTransferResults() throws JsonProcessingException;
}
