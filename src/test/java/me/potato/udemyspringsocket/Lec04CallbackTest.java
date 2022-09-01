package me.potato.udemyspringsocket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec04CallbackTest {
    private RSocketRequester         rSocketRequester;
    @Autowired
    private RSocketRequester.Builder builder;
    @Autowired
    private RSocketMessageHandler    handler;


    @BeforeAll
    public void setup() {
        this.rSocketRequester = builder
                .rsocketConnector(connector -> connector.acceptor(handler.responder()))
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    void callbackTest() throws InterruptedException {
        var mono = this.rSocketRequester.route("batch.job.request")
                .data(Mono.just(10))
                .send();

        StepVerifier
                .create(mono)
                .verifyComplete();

        Thread.sleep(5000);
    }

}
