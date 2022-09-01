package me.potato.udemyspringsocket.service;

import me.potato.udemyspringsocket.dto.ChartResponseDto;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MathService {
    // ff
    public Mono<Void> print(Mono<ComputationRequestDto> requestDtoMono) {
        return requestDtoMono
                .doOnNext(System.out::println)
                .then();
    }

    // rr
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestDtoMono) {
        return requestDtoMono
                .map(ComputationRequestDto::getInput)
                .map(input -> new ComputationResponseDto(input, input * input));
    }

    // rs
    public Flux<ComputationResponseDto> tableStream(ComputationRequestDto dto) {
        return Flux.range(1, 10)
                .map(i -> new ComputationResponseDto(dto.getInput(), dto.getInput() * i));
    }

    // rc : return x^2 + 1
    public Flux<ChartResponseDto> chartChannel(Flux<ComputationRequestDto> dto) {
        return dto
                .map(ComputationRequestDto::getInput)
                .map(input -> new ChartResponseDto(input, input * input + 1));
    }
}
