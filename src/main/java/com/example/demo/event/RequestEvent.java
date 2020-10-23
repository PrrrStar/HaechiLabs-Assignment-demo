package com.example.demo.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


/**
 * JsonIgnoreProperties 어노테이션으로 요청 받지 않은 param 은 무시합니다.
 * 그렇기 때문에 어떤 서비스 로직의 Request 가 들어와도 상관 없습니다.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestEvent {

    //Response Event
    private final String tx_hash;
    private final String tx_id;
    private final String amount;
    private final String from_address;
    private final String to_address;
    private final String wallet_id;
    private final String ticker;

    private final int deposit_id;
    private final int withdraw_id;

    public RequestEvent() {
        this(null, null,null,null,null,null,null,-1,-1);
    }

}
