package com.example.demo.domain;


import lombok.*;


/**
 * Deposit Mined 의 응답 Event 클래스 입니다.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ResponseMined {
    private final int deposit_id;

    public ResponseMined() {
        this(-1);
    }

}
