package com.projectreactor.reactor;

import com.projectreactor.reactor.mocks.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulersTest {

    @Test
    public void blockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        VideoAnalyser videoAnalyser = new VideoAnalyser();
        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();
        youtubeChannel
                .getAllVideos().log().filter(video ->
                {
                    System.out.println("Filter1 - Thread " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                }).map(video -> {
                            System.out.println("Map1 - Thread " + Thread.currentThread().getName());
                            return video.getDescription();
                        }
                ).map(description ->
                {
                    try {
                        Thread.sleep(5000);
                        System.out.println("Map2 - Thread " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return description.toUpperCase();

                }).subscribe(System.out::println);

        Thread.sleep(20_000);
    }


    @Test
    public void blockingOperation2() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        VideoAnalyser videoAnalyser = new VideoAnalyser();
        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();
        Flux<String> videos = youtubeChannel
                .getAllVideos().log().filter(video ->
                {
                    System.out.println("Filter1 - Thread " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                }).map(video -> {
                            System.out.println("Map1 - Thread " + Thread.currentThread().getName());
                            return video.getDescription();
                        }
                ).map(description ->
                {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Map2 - Thread " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return description.toUpperCase();

                });

        for (int i = 0; i < 2; i++) {
            System.out.println("Execucao > " + i);
            videos.subscribe(System.out::println);
        }

        Thread.sleep(20_000);
    }

    @Test
    public void PublishOnBlockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        Flux<String> videos = youtubeChannel
                .getAllVideos().log().filter(video ->
                {
                    System.out.println("Filter1 - Thread " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                }).map(video -> {
                    System.out.println("Map1 - Thread " + Thread.currentThread().getName());
                    return video.getDescription();
                })
                .publishOn(Schedulers.boundedElastic()) //tem que ser antes da operacao bloqueante
                .map(description -> {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Map2 - Thread " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return description.toUpperCase();

                });
//        videos.subscribe(System.out::println);
        for (int i = 0; i < 20; i++) {
            System.out.println("Execucao > " + i);
            videos.subscribe(System.out::println);
        }
        Thread.sleep(10_000);
    }

    @Test
    public void subscribeOnBlockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        Flux<String> videos = youtubeChannel
                .getAllVideos().log().filter(video ->
                {
                    System.out.println("Filter1 - Thread " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                }).map(video -> {
                    System.out.println("Map1 - Thread " + Thread.currentThread().getName());
                    return video.getDescription();
                })
                .subscribeOn(Schedulers.boundedElastic()) //tem que ser antes da operacao bloqueante
                .map(description -> {
                    try {
                        Thread.sleep(5000);
                        System.out.println("Map2 - Thread " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return description.toUpperCase();

                });
//        videos.subscribe(System.out::println);
        for (int i = 0; i < 2; i++) {
            System.out.println("Execucao > " + i);
            videos.subscribe(System.out::println);
        }
        Thread.sleep(10_000);
    }

    @Test
    public void parallelBlockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        VideoAnalyser videoAnalyser = new VideoAnalyser();

        youtubeChannel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - > Thread" + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                })
                .parallel().runOn(Schedulers.boundedElastic())
//                .publishOn(Schedulers.parallel())
                .map(video -> {
                    try {
                        return videoAnalyser.analyseBlocking(video);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe();

        Thread.sleep(10_000);
    }

    @Test
    public void parallelPublishOnBlockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        VideoAnalyser videoAnalyser = new VideoAnalyser();

        youtubeChannel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - > Thread" + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                })
                .flatMap(videoAnalyser::analyseBlockingMono)
//                .parallel().runOn(Schedulers.boundedElastic())
//                .publishOn(Schedulers.parallel())
//                .map(video -> {
//                    try {
//                        return videoAnalyser.analyseBlocking(video);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
                .subscribe();

        Thread.sleep(10_000);
    }
}
