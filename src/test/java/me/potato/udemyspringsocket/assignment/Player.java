package me.potato.udemyspringsocket.assignment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;

public class Player {

    private final Sinks.Many<Integer> sink    = Sinks.many().unicast().onBackpressureBuffer();
    private       int                 lower   = 0;
    private       int                 upper   = 100;
    private       int                 mid     = 0;
    private       int                 attempt = 0;

    public Flux<Integer> guesses() {
        return sink.asFlux();
    }

    public void play() {
        this.emit();
    }

    public Consumer<GuessNumberResponse> receives() {
        return this::processResponse;
    }

    private void processResponse(GuessNumberResponse response) {
        attempt++;
        System.out.println("Attempt: " + attempt + " mid: " + mid);

        if (GuessNumberResponse.EQUAL.equals(response)) {
            this.complete();
            return;
        }

        if (GuessNumberResponse.GREATER.equals(response)) {
            lower = mid;
        } else if (GuessNumberResponse.LESSER.equals(response)) {
            upper = mid;
        }

        this.emit();
    }

    private void emit() {
        mid = lower + (upper - lower) / 2;
        this.sink.tryEmitNext(mid);
    }

    private void complete() {
        this.sink.tryEmitComplete();
    }

}
