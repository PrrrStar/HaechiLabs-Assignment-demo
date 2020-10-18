package com.example.demo.client;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 코인 입출금 내역과 REST로 연결하기 위한
 * TransferEventClient 인터페이스 구현체
 */

@Component
public class TransferEventClientImpl implements TransferEventClient{


    private final RestTemplate restTemplate;
    private final HttpEntity<String> createHttpHeaders;
    private final String valueTransferEventsHost;

    @Autowired
    public TransferEventClientImpl(final RestTemplate restTemplate,
                                   final HttpEntity<String> createHttpHeaders,
                                   @Value("${valueTransferEventsHost}") String valueTransferEventsHost){
        this.restTemplate = restTemplate;
        this.createHttpHeaders = createHttpHeaders;
        this.valueTransferEventsHost = valueTransferEventsHost;
    }

    public TransferEventResultDTO retrieveTransferEventResultDTO() throws JsonProcessingException {
        String url = "http://localhost:3000/api/v2/eth/value-transfer-events";

        ResponseEntity<String> response = restTemplate.exchange(valueTransferEventsHost, HttpMethod.GET, createHttpHeaders, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        TransferEventResultDTO transferEventResultDTO = objectMapper.readValue(response.getBody(), TransferEventResultDTO.class);

        return transferEventResultDTO;
    }

    public ResponseEntity<String> retrieveTransferResults() throws JsonProcessingException {

        ResponseEntity<String> response = restTemplate.exchange(valueTransferEventsHost, HttpMethod.GET, createHttpHeaders, String.class);

        return response;
    }
}
