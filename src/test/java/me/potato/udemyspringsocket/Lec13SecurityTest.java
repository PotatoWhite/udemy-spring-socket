package me.potato.udemyspringsocket;

import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemyspringsocket.dto.ComputationRequestDto;
import me.potato.udemyspringsocket.dto.ComputationResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec13SecurityTest {

    private final MimeType                 mimetype = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
    private       RSocketRequester         requester;
    @Autowired
    private       RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        var metadata = new UsernamePasswordMetadata("client", "password");
        this.requester = builder
                .setupMetadata(metadata, mimetype)
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    public void requestResponse() {
        var credentials = new UsernamePasswordMetadata("admin", "password");
        var mono = requester
                .route("math.service.secured.square")
                .metadata(credentials, mimetype)
                .data(new ComputationRequestDto(5))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
    }

    @Test
    public void requestStream() {
        var credentials = new UsernamePasswordMetadata("admin", "password");
        var mono = requester
                .route("math.service.secured.table")
                .metadata(credentials, mimetype)
                .data(new ComputationRequestDto(5))
                .retrieveFlux(ComputationResponseDto.class)
                .doOnNext(System.out::println)
                .take(3);

        StepVerifier.create(mono).expectNextCount(3).verifyComplete();
    }
}
