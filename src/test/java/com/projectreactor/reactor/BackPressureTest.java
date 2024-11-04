package com.projectreactor.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackPressureTest {

    @Test
    public void backPressureBuffer() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1))
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Thread: " + Thread.currentThread().getName() + " Consumindo : " + number);
                    return number;
                }).log().subscribe();

        Thread.sleep(50_000);
    }

    @Test
    public void OnBackPressureBuffer() throws InterruptedException {
        System.setProperty("reactor.bufferSize.small", "16");
        Flux.interval(Duration.ofMillis(1))
                .onBackpressureBuffer(20) //se, o max size vai utilizar a memoria ate estourar a jvm
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Thread: " + Thread.currentThread().getName() + " Consumindo : " + number);
                    return number;
                }).log().subscribe();

        Thread.sleep(50_000);
    }


    @Test
    public void OnBackPressureError() throws InterruptedException {
        System.setProperty("reactor.bufferSize.small", "16");
        Flux.interval(Duration.ofMillis(1))
                .onBackpressureError()
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Thread: " + Thread.currentThread().getName() + " Consumindo : " + number);
                    return number;
                }).log().subscribe();

        Thread.sleep(50_000);
    }


    @Test
    public void OnBackPressureDrop() throws InterruptedException {
        System.setProperty("reactor.bufferSize.small", "16");
        Flux.interval(Duration.ofMillis(1))
                .onBackpressureDrop()
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Thread: " + Thread.currentThread().getName() + " Consumindo : " + number);
                    return number;
                }).log().subscribe();

        Thread.sleep(50_000);
    }

    @Test
    public void OnBackPressureLatest() throws InterruptedException {
        System.setProperty("reactor.bufferSize.small", "16");
        Flux.interval(Duration.ofMillis(1))
                .onBackpressureLatest()
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Thread: " + Thread.currentThread().getName() + " Consumindo : " + number);
                    return number;
                }).log().subscribe();

        Thread.sleep(50_000);
    }


    @Test
    public void createOperatorBackPressureStrategies_ERROR() throws InterruptedException {
        System.setProperty("reactor.bufferSize.small", "16");

        Flux<Object> fluxTest = Flux.create(emitter -> {
            for (int i = 0; i < 100; ++i) {
                emitter.next(i);
            }
            emitter.complete();
        }, FluxSink.OverflowStrategy.ERROR);

        fluxTest.publishOn(Schedulers.boundedElastic())
                .map(number -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Thread: " + Thread.currentThread().getName() + " Consumindo : " + number);
                    return number;
                }).log().subscribe();

        Thread.sleep(10_000);
    }

    @Test
    public void createOperatorBackPressureStrategies_DROP() throws InterruptedException {
        System.setProperty("reactor.bufferSize.small", "16");

        Flux<Object> fluxTest = Flux.create(emitter -> {
            for (int i = 0; i < 10_00000; ++i) {
                emitter.next(i);
            }
            emitter.complete();
        }, FluxSink.OverflowStrategy.DROP);

        fluxTest.publishOn(Schedulers.boundedElastic())
                .map(number -> {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Thread: " + Thread.currentThread().getName() + " Consumindo : " + number);
                    return number;
                }).log().subscribe();

        Thread.sleep(10_00000);
    }

    //Backpressure é um mecanismo de controle para lidar com a discrepância de velocidade entre a produção e o consumo de dados em fluxos reativos.
    //
    //
    //
    //onBackpressureBuffer(): Armazena os elementos em um buffer temporariamente quando não é solicitado demanda suficiente no downstream.
    //
    //onBackpressureError(): Gera um erro de backpressure imediatamente quando não é solicitado demanda suficiente no downstream.
    //
    //onBackpressureDrop(): Descarta elementos quando não é solicitado demanda suficiente no downstream.
    //
    //onBackpressureLatest(): Mantém apenas o último elemento emitido no buffer quando não é solicitado demanda suficiente no downstream, permitindo que o mais recente substitua os anteriores.

    @Test
    public void createOperatorBackPressureStrategies_LATEST() throws InterruptedException {
        System.setProperty("reactor.bufferSize.small", "16");

        Flux<Object> fluxTest = Flux.create(emitter -> {
            for (int i = 0; i < 10_00000; ++i) {
                emitter.next(i);
            }
            emitter.complete();
        }, FluxSink.OverflowStrategy.LATEST);

        fluxTest.publishOn(Schedulers.boundedElastic())
                .map(number -> {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Thread: " + Thread.currentThread().getName() + " Consumindo : " + number);
                    return number;
                }).log().subscribe();

        Thread.sleep(10_00000);
    }
}
