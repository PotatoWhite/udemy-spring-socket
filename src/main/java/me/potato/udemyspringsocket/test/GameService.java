package me.potato.udemyspringsocket.test;

import me.potato.udemyspringsocket.assignment.GuessNumberResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class GameService {
    public Flux<GuessNumberResponse> play(Flux<Integer> guessflux) {
        var number = ThreadLocalRandom.current().nextInt(1, 1000);
        System.out.println("Server Number:" + number + " " + Thread.currentThread().getName());
        return guessflux.map(i -> compare(number, i));
    }

    private GuessNumberResponse compare(int number, int guess) {
        if (number > guess)
            return GuessNumberResponse.GREATER;
        else if (number < guess)
            return GuessNumberResponse.LESSER;
        else
            return GuessNumberResponse.EQUAL;
    }
}
