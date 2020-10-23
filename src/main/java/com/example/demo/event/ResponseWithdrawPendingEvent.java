package com.example.demo.event;

import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ResponseWithdrawPendingEvent {
    private final int withdraw_id;

    public ResponseWithdrawPendingEvent() {
        this(-1);
    }

}