package com.example.demo.client;

import com.example.demo.client.dto.TransferEventResultDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;


/**
 * 코인 입출금 내역으로 부터 받아온 데이터를 역직렬화 한다.
 */
public class TransferEventDeserializer
        extends JsonDeserializer<TransferEventResultDTO> {


    @Override
    public TransferEventResultDTO deserialize(JsonParser jsonParser,
                                              DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        return new TransferEventResultDTO(
                node.get("results").get("id").asInt(),
                node.get("results").get("from").asText(),
                node.get("results").get("to").asText(),
                node.get("results").get("amount").asText(),
                node.get("results").get("blockchain").asText(),
                node.get("results").get("status").asText(),
                node.get("results").get("confirmation").asText(),
                node.get("results").get("walletId").asText(),
                node.get("results").get("orgId").asText(),
                node.get("results").get("masterWalletId").asText(),
                node.get("results").get("transactionId").asText(),
                node.get("results").get("coinSymbol").asText(),
                node.get("results").get("transferType").asText(),
                node.get("results").get("blockHash").asText(),
                node.get("results").get("transactionHash").asText(),
                node.get("results").get("createdAt").asText(),
                node.get("results").get("updatedAt").asText(),
                node.get("results").get("walletName").asText(),
                node.get("results").get("walletType").asText()
                );
    }
}
