package me.potato.udemyspringsocket.client.config;

import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.transport.ClientTransport;
import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemyspringsocket.client.serviceregistry.RSocketServiceInstance;
import me.potato.udemyspringsocket.client.serviceregistry.ServiceRegistryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class LoadBalanceTargetConfig {

    @Autowired
    private ServiceRegistryClient serviceRegistryClient;

    @Bean
    public Flux<List<LoadbalanceTarget>> targetFlux() {
        return Flux.from(targets());
    }

    private Mono<List<LoadbalanceTarget>> targets() {
        return Mono.fromSupplier(() -> serviceRegistryClient.getInstances()
                .stream()
                .map(server -> LoadbalanceTarget.from(key(server), transport(server)))
                .collect(Collectors.toList()));
    }

    private String key(RSocketServiceInstance instance) {
        return instance.getHost() + ":" + instance.getPort();
    }

    private ClientTransport transport(RSocketServiceInstance instance) {
        return TcpClientTransport.create(instance.getHost(), instance.getPort());
    }
}
