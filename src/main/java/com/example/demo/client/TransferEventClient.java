package com.example.demo.client;


import com.example.demo.client.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST 로 연결
 *
 */

@Component
public class TransferEventClient {
    private final RestTemplate restTemplate;
    private final String henesisWalletHost;
    private final String xHenesisSecret;
    private final String authorization;

    @Autowired
    public TransferEventClient(final RestTemplate restTemplate,
                               @Value("${henesisWalletHost}") final String henesisWalletHost,
                               @Value("${xHenesisSecret}") final String xHenesisSecret,
                               @Value("${authorization}") final String authorization){
        this.restTemplate = restTemplate;
        this.henesisWalletHost = henesisWalletHost;
        this.xHenesisSecret = xHenesisSecret;
        this.authorization = authorization;
    }
    private HttpHeaders createHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Henesis-Secret", xHenesisSecret);
        headers.add("Authorization", authorization);
        return headers;
    }

    public ResponseEntity<ResultDTO> resultDTO(){
        String url = henesisWalletHost+"/value-transfer-events";
        HttpHeaders headers = createHttpHeaders();
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ResultDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, ResultDTO.class);

        return responseEntity;
    }


}
