package me.potato.udemyspringsocket.client.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class BatchJobResponseController {

    @MessageMapping("batch.job.response")
    public Mono<Void> response(Mono<Integer> integerMono) {
        return integerMono
                .doOnNext(i -> System.out.println("Client Received: " + i))
                .then();
    }

}
