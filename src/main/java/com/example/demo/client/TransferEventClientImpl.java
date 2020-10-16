package com.example.demo.client;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * 코인 입출금 내역과 REST로 연결하기 위한
 * TransferEventClient 인터페이스 구현체
 */

@Component
public class TransferEventClientImpl implements TransferEventClient{


    private final RestTemplate restTemplate;
    private final String xHenesisSecret;
    private final String authorization;
    @Autowired
    public TransferEventClientImpl(final RestTemplate restTemplate,
                      @Value("${xHenesisSecret}") final String xHenesisSecret,
                      @Value("${authorization}") final String authorization){
        this.restTemplate = restTemplate;
        this.xHenesisSecret = xHenesisSecret;
        this.authorization = authorization;
    }

    //흠... 얘를 Bean 으로 빼고 싶은데......
    private HttpHeaders createHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Henesis-Secret", xHenesisSecret);
        headers.add("Authorization", authorization);
        return headers;
    }

    public TransferEventResultDTO retrieveTransferEventResultDTO() throws JsonProcessingException {
        String url = "http://localhost:3000/api/v2/eth/value-transfer-events";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> header = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, header, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        TransferEventResultDTO transferEventResultDTO = objectMapper.readValue(response.getBody(), TransferEventResultDTO.class);

        return transferEventResultDTO;
    }
}
