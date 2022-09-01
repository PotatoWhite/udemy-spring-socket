package me.potato.udemyspringsocket.controller;

import me.potato.udemyspringsocket.dto.Response;
import me.potato.udemyspringsocket.dto.error.ErrorEvent;
import me.potato.udemyspringsocket.dto.error.StatusCode;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("math.validation")
public class InputValidationController {
    @MessageMapping("double.{input}")
    public Mono<Integer> doubleIt(@DestinationVariable int input) {
        return Mono.just(input)
                .filter(i -> i < 31)
                .map(i -> i * 2)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("can not be greater than 30")));
    }

    @MessageMapping("double.response.{input}")
    public Mono<Response<Integer>> doubleResponse(@DestinationVariable int input) {
        return Mono.just(input)
                .filter(i -> i < 31)
                .map(i -> i * 2)
                .map(Response::with)
                .defaultIfEmpty(Response.with(new ErrorEvent(StatusCode.EC001)));
    }


    @MessageExceptionHandler
    public Mono<Integer> handleException(IllegalArgumentException e) {
        return Mono.just(-1);
    }
}
