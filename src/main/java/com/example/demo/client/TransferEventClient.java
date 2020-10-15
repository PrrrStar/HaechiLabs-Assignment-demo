package com.example.demo.client;


import com.example.demo.client.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * REST 로 연결
 *
 */

@Component
public class TransferEventClient {
    private final RestTemplate restTemplate;
    private final String henesisHost;
    private final String xHenesisSecret;
    private final String authorization;

    @Autowired
    public TransferEventClient(final RestTemplate restTemplate,
                               @Value("http://localhost:3000/api/v2/eth") final String henesisHost,
                               @Value("X3AlOxJyF+7inEG/oBku6Es2oqJnlc9ZDEdF8kgf03s=") final String xHenesisSecret,
                               @Value("Bearer "+"eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImptZWVmMDgwMkBnbWFpbC5jb20iLCJpZCI6IjI2ZTQyMjliOTQ0ZWZlNjNiYWU4MThlZDMyYTY0YTkxIiwidHlwZSI6IkxPTkciLCJsb25nVHlwZSI6dHJ1ZSwiaXNzIjoiaGVuZXNpcy13YWxsZXQtaWRlbnRpdHktcHJvZC10ZXN0bmV0IiwiaWF0IjoxNjAyNjgyODMyLCJleHAiOjE2MzQyMTg4MzJ9.IECkzlEUlB19o_6f2h8KZHRVsjTIXZoxZ5M5ueVOZMZC5HHsfW3c2NL5Z3BIKkgwQ8Wqx3DLJ76zrp0VWGieAg") final String authorization){
        this.restTemplate = restTemplate;
        this.henesisHost = henesisHost;
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

    public ResponseEntity<ResultDTO> retrieveResult(final long result){
        String url = henesisHost+"/value-transfer-events";
        HttpHeaders headers = createHttpHeaders();
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        System.out.println("url :\n"+url+"\nheaders :\n"+headers+"\nentity :\n"+entity);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ResultDTO.class
        );
    }


}
