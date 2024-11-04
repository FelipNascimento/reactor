package com.projectreactor.reactor;

import reactor.core.publisher.Mono;

import java.util.Random;

public class MonetizationCalculator {
    public Mono<Double> calculate(Video video) {
        System.out.println(video.getName() + " Views: " + video.getViews());

        if (video.getViews() == null) {
            return Mono.empty();
        }
        if (video.getViews() < 1000) {
            throw new RuntimeException();
        } else {
            return Mono.just(new Random().doubles(0, 2000).findFirst().getAsDouble());
        }
    }
}
