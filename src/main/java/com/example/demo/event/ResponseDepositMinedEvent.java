package com.example.demo.event;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ResponseDepositMinedEvent {
    private final int deposit_id;

    public ResponseDepositMinedEvent() {
        this(-1);
    }

}
