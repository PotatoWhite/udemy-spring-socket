package me.potato.udemyspringsocket;

import me.potato.udemyspringsocket.assignment.Game;
import me.potato.udemyspringsocket.assignment.GuessNumberResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec06Test {
    private RSocketRequester requester;
    @Autowired
    private RSocketRequester.Builder builder;


    @BeforeAll
    public void setup() {
        this.requester = builder.tcp("localhost", 6565);
    }

    @Test
    public void test() {
        var game = new Game();

        var rsocket = requester.route("game.guess")
                .data(game.guesses().delayElements(Duration.ofSeconds(2)))
                .retrieveFlux(GuessNumberResponse.class)
                .doFirst(game::start)
                .doOnNext(game::receive)
                .then();


        StepVerifier.create(rsocket)
                .verifyComplete();

    }
}
