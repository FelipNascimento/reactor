package com.projectreactor.reactor;

import com.projectreactor.reactor.mocks.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class CombineResultsTest {

    @Test
    public void concatVideosName() throws InterruptedException {
        Flux<String> videosName = new YoutubeChannel(MockVideo.generateVideos()).getAllVideosName()
                .delayElements(Duration.ofSeconds(1));
        Flux<String> videosName2 = new YoutubeChannel(MockVideo.generateVideos2()).getAllVideosName();

//        videosName.concatWith(videosName2).log().subscribe();


        Flux.concat(videosName, videosName2).log().subscribe();
        Thread.sleep(90000);

    }

    @Test
    public void mergeVideosName() throws InterruptedException {
        Flux<String> videosName = new YoutubeChannel(MockVideo.generateVideos()).getAllVideosName()
                .delayElements(Duration.ofMillis(500));
        Flux<String> videosName2 = new YoutubeChannel(MockVideo.generateVideos2()).getAllVideosName().delayElements(Duration.ofMillis(300));
        ;

//        videosName.mergeWith(videosName2).log().subscribe();

        Flux.merge(videosName, videosName2).log().subscribe();
        Thread.sleep(10000);

    }

    @Test
    public void zipVideosWithMoney() throws InterruptedException {
        Flux<String> videosName = new YoutubeChannel(MockVideo.generateVideos()).getAllVideosName()
                .delayElements(Duration.ofMillis(100)).log();
        Flux<Double> monetization = Flux.just(100.00, 500.00, 400.00, 600.00)
                .delayElements(Duration.ofSeconds(1)).log();
        ;

//                videosName.zipWith(monetization)
//                        .map(tuple -> tuple.getT1() + ", $: " + tuple.getT2()).log().subscribe();
//
        Flux.zip(videosName, monetization)
                .map(tuple -> tuple.getT1() + ", $: " + tuple.getT2()).log().subscribe();
        Thread.sleep(100000);

    }
}
