package me.potato.udemyspringsocket.test;

import lombok.RequiredArgsConstructor;
import me.potato.udemyspringsocket.assignment.GuessNumberResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;


@RequiredArgsConstructor
@Controller
public class GameController {
    private final GameService service;

    @MessageMapping("game.guess")
    public Flux<GuessNumberResponse> guess(Flux<Integer> guess) {
        return service.play(guess);
    }
}
