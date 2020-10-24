package com.example.demo.client;

import com.example.demo.client.dto.TransferEventResultDTO;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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


    /**
     * 서버에서 호출한 결과를 ResponseEntity<TransferEventResultDTO.Results> 타입으로 반환합니다.
     * @return ResponseEntity<TransferEventResultDTO.Results> response (ResponseEntity Type)
     */
    public ResponseEntity<TransferEventResultDTO> retrieveTransferResults(String url) {

        ResponseEntity<TransferEventResultDTO> response = restTemplate.exchange(url, HttpMethod.GET, createHttpHeaders, TransferEventResultDTO.class);
        return response;
    }

    public ResponseEntity<TransferEventResultDTO> detectTransferEvent(String url, String size, String page, String updatedAtGte) {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("page",page)
                .queryParam("size",size)
                .queryParam("updatedAtGte",updatedAtGte)
                .build(false);         //인코딩 False
        ResponseEntity<TransferEventResultDTO> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, createHttpHeaders, TransferEventResultDTO.class);
        return response;
    }

}
