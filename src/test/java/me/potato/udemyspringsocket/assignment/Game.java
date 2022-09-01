package me.potato.udemyspringsocket.assignment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;


public class Game {

    private Sinks.Many<Integer> sink = Sinks.many().unicast().onBackpressureBuffer();

    private int upper = 1000;
    private int mid   = 0;
    private int lower = 0;

    public Flux<Integer> guesses() {
        return this.sink.asFlux();
    }

    public void start() {
        emit();
    }

    public void receive(GuessNumberResponse response) {
        System.out.println("Number is " + response + " then " + mid + " " + Thread.currentThread().getName());

        if (GuessNumberResponse.EQUAL.equals(response)) {
            complete();
            return;
        }

        if (GuessNumberResponse.GREATER.equals(response))
            lower = mid;
        else if (GuessNumberResponse.LESSER.equals(response))
            upper = mid;

        emit();
    }

    private void emit() {
        mid = lower + (upper - lower) / 2;
        sink.tryEmitNext(mid);
    }

    private void complete() {
        sink.tryEmitComplete();
    }

}
