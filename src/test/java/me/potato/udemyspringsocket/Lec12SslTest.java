package me.potato.udemyspringsocket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.netty.tcp.TcpClient;
import reactor.test.StepVerifier;

@SpringBootTest
public class Lec12SslTest {

    static {
        System.setProperty("javax.net.ssl.trustStore", "/Users/user/Studyspace/udemy/udemy-spring-socket/ssl-tls/client.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
    }

    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    void sslTlsTest() throws InterruptedException {
        var requester = this.builder
                .transport(TcpClientTransport.create(
                        TcpClient.create().host("localhost").port(6565).secure()
                ));

        var mono = requester.route("math.service.square")
                .data(new ComputationRequestDto(5))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);


        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();


        Thread.sleep(2000);


    }
}
