package com.projectreactor.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class VideoLive {

    private String title;

    public VideoLive(String tittle) {
        this.title = tittle;
    }

    public Flux<String> play() {
        return Flux.interval(Duration.ofMillis(500))
                .map(value -> getLiveEvent(value))
                .takeWhile(event -> !event.equals("Fim!"))
                .publish()
                .autoConnect();
    }

    private String getLiveEvent(Long sequence) {
        switch (sequence.intValue()) {
            case 0:
                return "Inicio da live";
            case 1:
                return "Novo membro na live";
            case 2:
                return "Bate papo ao vivo...";
            case 3:
                return "Sorteio de brindes...";
            case 4:
                return "Proximo evento anunciado...";
            case 7:
                return "Nossa live chegou ao fim...";
            case 8:
                return "Fim!";
            default:
                return "Live está em andamento";
        }
    }

    public Flux<String> playN(int minSubscribers) {
        return Flux.interval(Duration.ofMillis(500))
                .map(value -> getLiveEvent(value))
                .takeWhile(event -> !event.equals("Fim!"))
                .publish()
                .autoConnect(minSubscribers);
    }

    public Flux<String> playReSubscription() {
        return Flux.interval(Duration.ofMillis(500))
                .map(value -> getLiveEvent(value))
                .takeWhile(event -> !event.equals("Fim!"))
                .share();
    }

    public Flux<String> playReCount(int minSubscribers) {
        return Flux.interval(Duration.ofMillis(500))
                .map(value -> getLiveEvent(value))
                .takeWhile(event -> !event.equals("Fim!"))
                .publish().refCount(minSubscribers);
    }
}
