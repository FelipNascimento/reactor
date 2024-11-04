package com.projectreactor.reactor;

import com.projectreactor.exception.MonetizationException;
import com.projectreactor.reactor.mocks.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;

public class ErrorHandlerTest {


    @Test
    public void onErrorReturnMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel.getAllVideos().flatMap(monetizationCalculator::calculate)
                .onErrorReturn(0.0)
                .subscribe(System.out::println);
    }

    @Test
    public void onErrorResumeMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel.getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .onErrorResume(exception -> {
                    System.out.println("onErrorResume");
                    return Flux.just(0.0, 999.00);
                })
                .subscribe(System.out::println);
    }

    @Test
    public void onErrorContinueMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel.getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .onErrorContinue((throwable, object) -> {
                    Video video = (Video) object;
                    System.out.println("OnErrorContinue:  " + video.getName());
                })
                .subscribe(System.out::println);
    }


    @Test
    public void onErrorMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();
        youtubeChannel.getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .onErrorMap(throwable -> {
                    System.out.println("OnErrorMap() ");
                    throw new MonetizationException("Less than 10k views");
                }).subscribe(value -> System.out.println("value: " + value));
    }

    @Test
    public void onErrorComplete() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();
        youtubeChannel.getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .onErrorComplete()
                .doFinally(signalType -> System.out.println("Signal Type: " + signalType))
                .subscribe(itemOnErrorComplete -> System.out.println("OnErrorCompleteItem: " + itemOnErrorComplete));
    }


    @Test
    public void isEmptyMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel.getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .defaultIfEmpty(0.0)
                .subscribe(System.out::println);
    }

    @Test
    public void swotchIfEmptyMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel.getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .switchIfEmpty(Flux.just(0.0, 1.0))
                .subscribe(System.out::println);
    }

    @Test
    public void retryVideoAnalyser() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());
        VideoAnalyser videoAnalyser = new VideoAnalyser();
        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel.getAllVideos()
                .log()
                .map(videoAnalyser::analyse)
                .retry(1)
                .subscribe(System.out::println);
    }

    @Test
    public void retryWhenVideoAnalyser() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());
        VideoAnalyser videoAnalyser = new VideoAnalyser();
        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel.getAllVideos()
                .log()
                .map(videoAnalyser::analyse)
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)))
                .subscribe(System.out::println);


        Thread.sleep(10000);
    }

}
