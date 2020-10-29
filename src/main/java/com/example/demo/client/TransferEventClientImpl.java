package com.example.demo.client;

import com.example.demo.client.dto.TransferEventResultDTO;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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
     * Uri Components 를 이용해서 요청 Parameter 넣은 URI 를 생성합니다.
     * 생성한 URI 를 Rest template 에 추가해서
     * Configuration 에서 생성한 header 와 같이 exchange 한 뒤 그 결과를 반환합니다.
     * @param url
     * @param size
     * @param page
     * @param updatedAtGte
     * @return ResponseEntity<TransferEventResultDTO> response
     */
    public ResponseEntity<TransferEventResultDTO> detectTransferEvent(String url,
                                                                      String size,
                                                                      String page,
                                                                      String status,
                                                                      String walletId,
                                                                      String masterWalletId,
                                                                      String updatedAtGte) {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("size",size)
                .queryParam("page",page)
                .queryParam("status",status)
                .queryParam("walletId",walletId)
                .queryParam("masterWalletId",masterWalletId)
                .queryParam("updatedAtGte",updatedAtGte)

                .build(false);         //인코딩 False
        ResponseEntity<TransferEventResultDTO> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, createHttpHeaders, TransferEventResultDTO.class);
        return response;
    }



    /**
     * 서버에서 호출한 결과를 ResponseEntity<TransferEventResultDTO.Results> 타입으로 반환합니다.
     * @return ResponseEntity<TransferEventResultDTO.Results> response (ResponseEntity Type)
     */
    @Retryable(maxAttempts =2, backoff=@Backoff(delay = 1), value= IllegalStateException.class)
    public ResponseEntity<TransferEventResultDTO> retrieveTransferResults(String url) {

        ResponseEntity<TransferEventResultDTO> response = restTemplate.exchange(url, HttpMethod.GET, createHttpHeaders, TransferEventResultDTO.class);
        return response;
    }



}
