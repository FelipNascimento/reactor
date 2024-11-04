package com.projectreactor.reactor;

import com.projectreactor.reactor.mocks.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class HotColdPublisherTest {

    @Test
    public void testColdPublisher() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().subscribe(value -> System.out.println("Subscribe 1 " + value));
        System.out.println("--------------------------------------------");
        youtubeChannel.getAllVideos().subscribe(value -> System.out.println("Subscribe 2 " + value));
    }

    @Test
    public void testHotPublishAutoConnect() throws InterruptedException {
        VideoLive videoLive = new VideoLive("MeetUp Java 21");
        Flux<String> java21Live = videoLive.play();

        java21Live.subscribe(value -> System.out.println("Usuario 1: " + value));
        Thread.sleep(2000);
        java21Live.subscribe(value -> System.out.println("Usuario 2: " + value));
        Thread.sleep(4000);
        java21Live.subscribe(value -> System.out.println("Usuario 3: " + value));

        Thread.sleep(20_0000);

    }

    @Test
    public void testHotPublishAutoConnectAny() throws InterruptedException {
        VideoLive videoLive = new VideoLive("MeetUp Java 21");
        Thread.sleep(2000);
        Flux<String> java21Live = videoLive.playN(3);
        Thread.sleep(2000);
        java21Live.subscribe(value -> System.out.println("Usuario 1: " + value));
        Thread.sleep(2000);
        java21Live.subscribe(value -> System.out.println("Usuario 2: " + value));
        Thread.sleep(4000);
        java21Live.subscribe(value -> System.out.println("Usuario 3: " + value));

        Thread.sleep(20_0000);

    }

    @Test
    public void testHotPublishReSubscription() throws InterruptedException {
        VideoLive videoLive = new VideoLive("MeetUp Java 21");
        Thread.sleep(2000);
        Flux<String> java21Live = videoLive.playReSubscription();
        Thread.sleep(2000);
        java21Live.subscribe(value -> System.out.println("Usuario 1: " + value));
        Thread.sleep(2000);
        java21Live.subscribe(value -> System.out.println("Usuario 2: " + value));
        Thread.sleep(4000);
        java21Live.subscribe(value -> System.out.println("Usuario 3: " + value));

        Thread.sleep(20_0000);

    }

    @Test
    public void testHotPublishRefCount() throws InterruptedException {
        VideoLive videoLive = new VideoLive("MeetUp Java 21");

        Flux<String> java21Live = videoLive.playReCount(1);
        Thread.sleep(2000);
        java21Live.subscribe(value -> System.out.println("Usuario 1: " + value));
        Thread.sleep(2000);
        java21Live.subscribe(value -> System.out.println("Usuario 2: " + value));
        Thread.sleep(4000);
        java21Live.subscribe(value -> System.out.println("Usuario 3: " + value));

        Thread.sleep(10_0000);

    }

}
