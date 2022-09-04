package me.potato.udemyspringsocket;

import io.rsocket.core.Resume;
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
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
})
public class Lec09SessionResumptionTest {
    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void connectionTest() throws InterruptedException {
        RSocketRequester requester = this.builder
                .rsocketConnector(c -> c
                        .resume(resumeStrategy())
                        .reconnect(retryStrategy()))
                .transport(TcpClientTransport.create("localhost", 6566));

        var flux = requester.route("math.service.table")
                .data(new ComputationRequestDto(5))
                .retrieveFlux(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(flux)
                .expectNextCount(1000)
                .verifyComplete();
    }

    private Resume resumeStrategy() {
        return new Resume()
                .retry(Retry.fixedDelay(2000, Duration.ofSeconds(2))
                        .doBeforeRetry(s -> System.out.println("resume - retry: " + s.totalRetriesInARow())));
    }

    private Retry retryStrategy() {
        return Retry.fixedDelay(10, Duration.ofSeconds(2))
                .doBeforeRetry(s -> System.out.println("retrying..." + s.totalRetriesInARow()));
    }

}
