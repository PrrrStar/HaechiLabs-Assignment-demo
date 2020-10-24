package com.example.demo.event;


import lombok.*;


/**
 * Deposit Mined 의 응답 Event 클래스 입니다.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ResponseMinedEvent {
    private final int deposit_id;

    public ResponseMinedEvent() {
        this(-1);
    }

}
