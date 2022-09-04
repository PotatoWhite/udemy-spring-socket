package me.potato.udemyspringsocket.security;

import lombok.RequiredArgsConstructor;
import me.potato.udemyspringsocket.dto.ChartResponseDto;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import me.potato.udemyspringsocket.service.MathService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@MessageMapping("math.service.secured")
public class SecureMathController {
    private final MathService mathService;

    @PreAuthorize("hasRole('USER')")
    @MessageMapping("square")
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> dtoMono, @AuthenticationPrincipal Mono<UserDetails> userDetailsMono) {
        userDetailsMono.doOnNext(System.out::println).subscribe();
        return mathService.findSquare(dtoMono);
    }

    @MessageMapping("table")
    public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> dtoMono) {
        return dtoMono.flatMapMany(mathService::tableStream);
    }
}
