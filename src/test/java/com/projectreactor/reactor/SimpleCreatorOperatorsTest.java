package com.projectreactor.reactor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class SimpleCreatorOperatorsTest {

    @Test
    public void fluxJust() {
        Flux<Integer> simpleFux = Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofMillis(1000)).log();

        StepVerifier.create(simpleFux)
                .expectNext(1, 2, 3, 4, 5, 6)
                .verifyComplete();
    }

    @Test
    public void fluxFromIterable() {
        Flux fluxIterable = Flux.fromIterable(List.of(1, 2, 3, 4)).log();


        StepVerifier.create(fluxIterable)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }


    @Test
    public void fluxFromArray() {
        Flux fluxIterable = Flux.fromArray(new Integer[]{1, 2, 3, 4}).log();


        StepVerifier.create(fluxIterable)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void fluxFromStream() {
        Flux fluxIterable = Flux.fromStream(Stream.of(1, 2, 3, 4)).log();


        StepVerifier.create(fluxIterable)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }


    @Test
    public void fluxFromRange() {
        Flux fluxIterable = Flux.range(1, 4).log();


        StepVerifier.create(fluxIterable)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void MonoJust() {
        Mono monoJust = Mono.just(1).log();


        StepVerifier.create(monoJust)
                .expectNext(1)
                .verifyComplete();
    }
}
