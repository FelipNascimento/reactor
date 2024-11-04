package com.projectreactor.reactor;

public class SubscriberTest {
    public static void main(String[] args) {
        YoutubeChannel publisher = new YoutubeChannel();

        publisher.addVideo(new Video("Primeiro Video", "Teste Primeiro video Descrição", 10000, 1000000));
        publisher.addVideo(new Video("Segundo Video", "Teste Segundo Video Descrição", 5555, 555555));

        User subscriber = new User("Felipe");

//        publisher.getAllVideos().subscribe(subscriber);

//        publisher.getAllVideos().log().subscribe();
        publisher.getAllVideos().log().subscribe(
                System.out::println,
                throwable -> System.out.println("" + throwable),
                () -> System.out.println("dados exibidos com sucesso")
        );


    }

}
