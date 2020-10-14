package com.example.demo.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@RestController
@RequestMapping(value= "/notifications", method= RequestMethod.GET, produces = "application/json;")
public class DepositAPIController {


    private HttpHeaders createHttpHeaders(){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Henesis-Secret", "X3AlOxJyF+7inEG/oBku6Es2oqJnlc9ZDEdF8kgf03s=");
            headers.add("Authorization","Bearer "+"eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImptZWVmMDgwMkBnbWFpbC5jb20iLCJpZCI6IjI2ZTQyMjliOTQ0ZWZlNjNiYWU4MThlZDMyYTY0YTkxIiwidHlwZSI6IkxPTkciLCJsb25nVHlwZSI6dHJ1ZSwiaXNzIjoiaGVuZXNpcy13YWxsZXQtaWRlbnRpdHktcHJvZC10ZXN0bmV0IiwiaWF0IjoxNjAyNjgyODMyLCJleHAiOjE2MzQyMTg4MzJ9.IECkzlEUlB19o_6f2h8KZHRVsjTIXZoxZ5M5ueVOZMZC5HHsfW3c2NL5Z3BIKkgwQ8Wqx3DLJ76zrp0VWGieAg");
            return headers;
        }


    @RequestMapping(value= "/deposit")
    @ResponseStatus(value = HttpStatus.OK)
    public MultiValueMap<String, Object> DepositAPI() throws JsonProcessingException {


        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String url = "http://localhost:3000/api/v2/eth/value-transfer-events";
        String jsonInString = "";


        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> header = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, header, Map.class);

        //System.out.println(mapper.readValue(response, TransactionStatus.class));


        ArrayList<Map> lm = (ArrayList<Map>) response.getBody().get("results");
        jsonInString = mapper.writeValueAsString(lm);

        JsonNode rootNode = mapper.readTree(jsonInString);


        Iterator<JsonNode> itr = rootNode.iterator();

        MultiValueMap<String, Object> depositJson = new LinkedMultiValueMap<>();

        HashMap<String, Object> depositMap = new HashMap<String, Object>();

        while(itr.hasNext()){
            JsonNode paramNode = itr.next();
            if(paramNode.findValue("transferType").asText().equals("DEPOSIT")){
                depositMap.put("type",paramNode.findValue("transferType"));
                depositMap.put("tx_hash",paramNode.findValue("transactionHash"));
                depositMap.put("amount",paramNode.findValue("amount"));
                depositMap.put("from_address",paramNode.findValue("from"));
                depositMap.put("to_address",paramNode.findValue("to"));
                depositMap.put("wallet_id",paramNode.findValue("masterWalletId"));
                depositMap.put("ticker",paramNode.findValue("coinSymbol"));

                depositJson.add("deposit",depositMap);

            }

        }
        return depositJson;
    }
}
