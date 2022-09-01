package me.potato.udemyspringsocket.controller;

import lombok.RequiredArgsConstructor;
import me.potato.udemyspringsocket.dto.ChartResponseDto;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import me.potato.udemyspringsocket.service.MathService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
public class MathController {
    private final MathService mathService;


    @MessageMapping("math.service.print")
    public Mono<Void> print(Mono<ComputationRequestDto> dtoMono) {
        return mathService.print(dtoMono);
    }

    @MessageMapping("math.service.square")
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> dtoMono) {
        return mathService.findSquare(dtoMono);
    }

    @MessageMapping("math.service.table")
    public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> dtoMono) {
        return dtoMono.flatMapMany(mathService::tableStream);

    }

    @MessageMapping("math.service.chart")
    public Flux<ChartResponseDto> chartChannel(Flux<ComputationRequestDto> dtoFlux) {
        return mathService.chartChannel(dtoFlux);
    }
}
