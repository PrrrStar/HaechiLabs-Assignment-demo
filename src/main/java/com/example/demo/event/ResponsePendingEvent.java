package com.example.demo.event;

import lombok.*;


/**
 * Withdraw Pending 의 응답 Event 클래스 입니다.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ResponsePendingEvent {
    private final int withdraw_id;

    public ResponsePendingEvent() {
        this(-1);
    }

}