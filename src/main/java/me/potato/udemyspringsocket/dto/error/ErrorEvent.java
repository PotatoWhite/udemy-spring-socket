package me.potato.udemyspringsocket.dto.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ErrorEvent {
    private       StatusCode    statusCode;
    private final LocalDateTime time = LocalDateTime.now();

    public ErrorEvent(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
