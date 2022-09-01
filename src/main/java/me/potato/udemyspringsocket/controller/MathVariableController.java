package me.potato.udemyspringsocket.controller;

import lombok.RequiredArgsConstructor;
import me.potato.udemyspringsocket.dto.ChartResponseDto;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import me.potato.udemyspringsocket.service.MathService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@MessageMapping("math.service")
public class MathVariableController {

    private final MathService mathService;

    @MessageMapping("print.{input}")
    public Mono<Void> print(@DestinationVariable int input) {
        System.out.println("Received: " + input);
        return Mono.empty();
    }

    @MessageMapping("square.{input}")
    public Mono<ComputationResponseDto> findSquare(@DestinationVariable int input) {
        return Mono.just(new ComputationResponseDto(input, input * input));
    }

    @MessageMapping("table.{input}")
    public Flux<ComputationResponseDto> tableStream(@DestinationVariable int input) {
        return Flux.range(1, 10)
                .map(i -> new ComputationResponseDto(input, input * i));
    }

    @MessageMapping("chart.{input}")
    public Flux<ChartResponseDto> chartChannel(@DestinationVariable int input) {
        return mathService.chartChannel(Flux.just(new ComputationRequestDto(input)));
    }

}
