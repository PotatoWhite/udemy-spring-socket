package me.potato.udemyspringsocket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemyspringsocket.dto.ChartResponseDto;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec01RSocketTest {

    @Autowired
    private RSocketRequester.Builder builder;
    private RSocketRequester         requester;

    @BeforeAll
    public void setup() {
        this.requester = builder.transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    void fireAndForget() {
        var ff = requester.route("math.service.print")
                .data(Mono.just(new ComputationRequestDto(1)))
                .send();

        StepVerifier.create(ff)
                .verifyComplete();
    }

    @Test
    void requestResponse() {
        var rr = requester.route("math.service.square")
                .data(Mono.just(new ComputationRequestDto(1)))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(rr)
                .expectNext(new ComputationResponseDto(1, 1))
                .verifyComplete();
    }

    @Test
    void requestStream() {
        var rr = requester.route("math.service.table")
                .data(Mono.just(new ComputationRequestDto(1)))
                .retrieveFlux(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(rr)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void requestChannel() {
        var requestDto = Flux.range(-10, 21).map(ComputationRequestDto::new).delayElements(Duration.ofMillis(100));
        var rr = requester.route("math.service.chart")
                .data(requestDto)
                .retrieveFlux(ChartResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(rr)
                .expectNextCount(21)
                .verifyComplete();
    }

}
