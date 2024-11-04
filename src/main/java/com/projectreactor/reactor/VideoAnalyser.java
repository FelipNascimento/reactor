package com.projectreactor.reactor;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

public class VideoAnalyser {
    Double analyse(Video video) {
        Double rate = new Random().doubles(1, 15).findFirst().getAsDouble();

        System.out.println(video.getName() + " rate" + rate);

        if (rate > 10) {
            throw new RuntimeException("An unexpected error occured");
        }

        return rate;
    }

    Double analyseBlocking(Video video) throws InterruptedException {
        Thread.sleep(3);
        Double rate = new Random().doubles(1, 10).findFirst().getAsDouble();

        System.out.println(video.getName() + " rate" + rate + " Thread : " + Thread.currentThread().getName());

        if (rate > 10) {
            throw new RuntimeException("An unexpected error occured");
        }

        return rate;
    }

    //publishOn(Scheduler scheduler): Usado para definir o scheduler onde as operações subsequentes de um fluxo (como map, filter, etc.) serão executadas.
    //
    //subscribeOn(Scheduler scheduler): Usado para definir o scheduler onde todas as operações serão afetadas (upstream e downstream).
    //
    //parallel().runOn(Scheduler scheduler): Permite executar operações simultaneamente em paralelo em várias threads.
    //
    //Mono.fromCallable(() -> function()).publishOn(Scheduler scheduler): Recebe uma função e produz um Mono que é executado em uma thread paralela.

    //usando fromCallable que recebe uma funcao e transforma num mono
    Mono<Double> analyseBlockingMono(Video video) {
        return Mono.fromCallable(() -> analyseBlocking(video)).publishOn(Schedulers.boundedElastic());
    }
}
