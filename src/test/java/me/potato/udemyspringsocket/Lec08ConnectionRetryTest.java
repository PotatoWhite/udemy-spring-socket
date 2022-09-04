package me.potato.udemyspringsocket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

@SpringBootTest
@TestPropertySource(properties =
        {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
        }
)
public class Lec08ConnectionRetryTest {
    @Autowired
    private RSocketRequester.Builder builder;


    @Test
    public void connectionTest() throws InterruptedException {

        var requester1 = this.builder
                .rsocketConnector(connector -> connector.reconnect(Retry.fixedDelay(10, Duration.ofSeconds(2))
                        .doBeforeRetry(s -> System.out.println("retrying..." + s.totalRetriesInARow()))))
                .transport(TcpClientTransport.create("localhost", 6565));

        for (int i = 0; i < 50; i++) {
            var mono = requester1.route("math.service.square")
                    .data(new ComputationRequestDto(i))
                    .retrieveMono(ComputationResponseDto.class)
                    .doOnNext(System.out::println);

            StepVerifier.create(mono)
                    .expectNext(new ComputationResponseDto(i, i*i))
                    .verifyComplete();


            Thread.sleep(2000);
        }



    }

}
