package me.potato.udemyspringsocket.client.serviceregistry;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRegistryClient {
    private List<RSocketServiceInstance> instances;

    public ServiceRegistryClient() {
        this.instances = List.of(
                new RSocketServiceInstance("localhost", 7070),
                new RSocketServiceInstance("localhost", 7071),
                new RSocketServiceInstance("localhost", 7072)
        );
    }

    public List<RSocketServiceInstance> getInstances() {
        return instances;
    }
}
