package me.potato.udemyspringsocket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemyspringsocket.dto.ClientConnectionRequest;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec06ConnectionSetupTest {
    private RSocketRequester         requester;
    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        var request = new ClientConnectionRequest();
        request.setClientId("order-service");
        request.setSecretKey("password");


        this.requester = builder
                .setupData(request)
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @RepeatedTest(3)
    public void connectionTest() {
        var mono = this.requester.route("math.service.square")
                .data(new ComputationRequestDto(ThreadLocalRandom.current().nextInt(1, 50)))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }

}
