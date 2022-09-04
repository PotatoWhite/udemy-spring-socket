package me.potato.udemyspringsocket;


import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.loadbalance.RoundRobinLoadbalanceStrategy;
import io.rsocket.loadbalance.WeightedLoadbalanceStrategy;
import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemyspringsocket.client.config.LoadBalanceTargetConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
})
public class Lec11ClientSideLoadBalancingTest {
    @Autowired
    private RSocketRequester.Builder builder;

    @Autowired
    private Flux<List<LoadbalanceTarget>> targets;


    @Test
    void connectionTest() throws InterruptedException {
        var requester = this.builder.transports(targets, WeightedLoadbalanceStrategy.create());

        for (int i = 0; i < 50; i++) {
            var flux1 = requester.route("math.service.print")
                    .data(i)
                    .send()
                    .subscribe();


            Thread.sleep(2000);
        }


    }

}
