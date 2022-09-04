package me.potato.udemyspringsocket.controller;

import lombok.RequiredArgsConstructor;
import me.potato.udemyspringsocket.service.MathClientManager;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
public class ConnectionHandler {

    private final MathClientManager mathClientManager;

//    @ConnectMapping
//    public Mono<Void> noEventConnection(RSocketRequester requester) {
//        System.out.println("noEvent Connection Established");
//        return Mono.empty();
//    }


    @ConnectMapping
    public Mono<Void> noEventConnection(RSocketRequester requester) {
        System.out.println("noEvent Connection Established");
        return Mono.empty();
    }

    @ConnectMapping("math.event.connection")
    public Mono<Void> mathEventConnection(RSocketRequester requester) {
        System.out.println("mathEvent Connection Established");
        return Mono.fromRunnable(() -> mathClientManager.add(requester));
    }
}
