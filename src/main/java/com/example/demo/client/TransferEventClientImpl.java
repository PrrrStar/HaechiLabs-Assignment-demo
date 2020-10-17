package com.example.demo.client;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * 코인 입출금 내역과 REST로 연결하기 위한
 * TransferEventClient 인터페이스 구현체
 */

@Component
public class TransferEventClientImpl implements TransferEventClient{


    private final RestTemplate restTemplate;
    private final HttpEntity<String> createHttpHeaders;

    @Autowired
    public TransferEventClientImpl(final RestTemplate restTemplate,
                                   final HttpEntity<String> createHttpHeaders){
        this.restTemplate = restTemplate;
        this.createHttpHeaders = createHttpHeaders;
    }


    @Scheduled(fixedDelay = 1000)
    public TransferEventResultDTO retrieveTransferEventResultDTO() throws JsonProcessingException {
        String url = "http://localhost:3000/api/v2/eth/value-transfer-events";

        ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, createHttpHeaders, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        TransferEventResultDTO transferEventResultDTO = objectMapper.readValue(response.getBody(), TransferEventResultDTO.class);

        return transferEventResultDTO;


    }

}
