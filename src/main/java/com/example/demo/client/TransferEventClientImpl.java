package com.example.demo.client;


import com.example.demo.client.dto.TransferEventResultDTO;
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
    private final String henesisWalletHost;
    private final String xHenesisSecret;
    private final String authorization;

    @Autowired
    public TransferEventClientImpl(final RestTemplate restTemplate,
                                   @Value("${henesisWalletHost}") final String henesisWalletHost,
                                   @Value("${xHenesisSecret}") final String xHenesisSecret,
                                   @Value("${authorization}") final String authorization){
        this.restTemplate = restTemplate;
        this.henesisWalletHost = henesisWalletHost;
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


    @Override
    public ResponseEntity<TransferEventResultDTO> retrieveTransferEventResultDTOById(Long transferEventId) {
        String url = henesisWalletHost+"/value-transfer-events";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<TransferEventResultDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, TransferEventResultDTO.class);

        return responseEntity;
    }
}
