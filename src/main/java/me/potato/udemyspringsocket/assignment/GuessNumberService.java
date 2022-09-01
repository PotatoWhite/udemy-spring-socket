package me.potato.udemyspringsocket.assignment;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class GuessNumberService {

    public Flux<GuessNumberResponse> play(Flux<Integer> flux) {
        var serverNumber = ThreadLocalRandom.current().nextInt(1, 100);
        System.out.println("Server Number: " + serverNumber);
        return flux.map(i -> compare(serverNumber, i));
    }

    private GuessNumberResponse compare(int serverNumber, int clientNumber) {
        if (serverNumber == clientNumber) {
            return GuessNumberResponse.EQUAL;
        } else if (serverNumber > clientNumber) {
            return GuessNumberResponse.GREATER;
        } else {
            return GuessNumberResponse.LESSER;
        }
    }
}
