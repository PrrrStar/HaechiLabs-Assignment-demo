package com.example.demo.domain;

import lombok.*;


/**
 * Withdraw Pending 의 응답 Event 클래스 입니다.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ResponsePending {
    private final int withdraw_id;

    public ResponsePending() {
        this(-1);
    }

}