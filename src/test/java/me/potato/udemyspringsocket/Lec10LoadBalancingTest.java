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

@SpringBootTest
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
})
public class Lec10LoadBalancingTest {

    @Autowired
    private RSocketRequester.Builder builder;


    @Test
    void connectionTest() throws InterruptedException {
        var requester1 = this.builder
                .transport(TcpClientTransport.create("localhost", 6566));

        var requester2 = this.builder
                .transport(TcpClientTransport.create("localhost", 6566));


        for (int i = 0; i < 50; i++) {
            var flux1 = requester1.route("math.service.print")
                    .data(new ComputationRequestDto(i))
                    .send()
                    .subscribe();

            var flux2 = requester2.route("math.service.print")
                    .data(new ComputationRequestDto(i))
                    .send()
                    .subscribe();

            Thread.sleep(2000);
        }


    }

}
