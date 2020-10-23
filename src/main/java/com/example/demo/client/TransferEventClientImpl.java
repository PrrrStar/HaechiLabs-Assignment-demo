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

    /**
     * DTO 와 ResponseEntity 를 매핑한 결과를 DTO 객체 타입으로 반환합니다.
     * @return TransferEventResultDTO transferEventResultDTO (DTO Object Type)
     * @throws JsonProcessingException
     */
    public TransferEventResultDTO retrieveTransferEventResultDTO() throws JsonProcessingException {

        ResponseEntity<String> response = restTemplate.exchange(valueTransferEventsHost, HttpMethod.GET, createHttpHeaders, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        TransferEventResultDTO transferEventResultDTO = objectMapper.readValue(response.getBody(), TransferEventResultDTO.class);

        return transferEventResultDTO;
    }

    /**
     * 서버에서 호출한 결과를 ResponseEntity<TransferEventResultDTO.Results> 타입으로 반환합니다.
     * @return ResponseEntity<TransferEventResultDTO.Results> response (ResponseEntity Type)
     */
    public ResponseEntity<TransferEventResultDTO.Results> retrieveTransferResults() {

        ResponseEntity<TransferEventResultDTO.Results> response = restTemplate.exchange(valueTransferEventsHost, HttpMethod.GET, createHttpHeaders, TransferEventResultDTO.Results.class);

        return response;
    }

    /**
     * 서버에서 호출한 페이징 결과를 ResponseEntity<TransferEventResultDTO.Pagination> 타입으로 반환합니다.
     * @return ResponseEntity<TransferEventResultDTO.Pagination> response (ResponseEntity Type)
     */
    public ResponseEntity<TransferEventResultDTO.Pagination> retrievePagiantion(){
        ResponseEntity<TransferEventResultDTO.Pagination> response = restTemplate.exchange(valueTransferEventsHost, HttpMethod.GET, createHttpHeaders, TransferEventResultDTO.Pagination.class);

        return response;
    }

}
