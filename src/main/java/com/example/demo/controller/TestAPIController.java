package com.example.demo.controller;


import com.example.demo.client.dto.TransferEventResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value= "/api", method= RequestMethod.GET, produces = "application/json;")
public class TestAPIController {


    private HttpHeaders createHttpHeaders(){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Henesis-Secret", "X3AlOxJyF+7inEG/oBku6Es2oqJnlc9ZDEdF8kgf03s=");
            headers.add("Authorization","Bearer "+"eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImptZWVmMDgwMkBnbWFpbC5jb20iLCJpZCI6IjI2ZTQyMjliOTQ0ZWZlNjNiYWU4MThlZDMyYTY0YTkxIiwidHlwZSI6IkxPTkciLCJsb25nVHlwZSI6dHJ1ZSwiaXNzIjoiaGVuZXNpcy13YWxsZXQtaWRlbnRpdHktcHJvZC10ZXN0bmV0IiwiaWF0IjoxNjAyNjgyODMyLCJleHAiOjE2MzQyMTg4MzJ9.IECkzlEUlB19o_6f2h8KZHRVsjTIXZoxZ5M5ueVOZMZC5HHsfW3c2NL5Z3BIKkgwQ8Wqx3DLJ76zrp0VWGieAg");
            return headers;
        }
    @RequestMapping("/test")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferEventResultDTO testAPI() throws JsonProcessingException {


        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String url = "http://localhost:3000/api/v2/eth/value-transfer-events";
        String jsonInString = "";


        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> header = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, header, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        TransferEventResultDTO transferEventResultDTO = objectMapper.readValue(response.getBody(), TransferEventResultDTO.class);


        return transferEventResultDTO;
    }
}
