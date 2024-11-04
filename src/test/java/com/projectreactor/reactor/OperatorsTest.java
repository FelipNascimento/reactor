package com.projectreactor.reactor;

import com.projectreactor.reactor.mocks.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class OperatorsTest {

    @Test
    public void operatorsTest() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        youtubeChannel.getAllVideos().log().subscribe(System.out::println);

    }

    @Test
    public void printVideosTake() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.generateVideos());
        channel.getAllVideos(2).subscribe(System.out::println);

    }

    @Test
    public void printVideosTakeWhile() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.generateVideos());
        channel.getAllVideos().log()
                .takeWhile(video -> video.getLikes() > 200)
                .subscribe(videos -> System.out.println(videos.getName()));

    }

    @Test
    public void printDescriptionSize() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.generateVideos());
        channel.getDescriptionSize().log().subscribe(integer -> System.out.println("Tamanho descricao: " + integer));

    }

    @Test
    public void printVideoName() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.generateVideos());
        channel.getAllVideosName().log().subscribe();
    }

    @Test
    public void badFlatMap() {
        List<YoutubeChannel> youtubeChannels = Arrays.asList(new YoutubeChannel(MockVideo.generateVideos()),
                new YoutubeChannel(MockVideo.generateVideos2()));
        Flux<YoutubeChannel> fluxYouTubeChannel = Flux.fromIterable(youtubeChannels);

        fluxYouTubeChannel.map(videos -> videos.getAllVideosName().log()).subscribe(item -> System.out.println(item));


    }

    @Test
    public void flatMap() {
        List<YoutubeChannel> youtubeChannels = Arrays.asList(new YoutubeChannel(MockVideo.generateVideos()),
                new YoutubeChannel(MockVideo.generateVideos2()));

        Flux<YoutubeChannel> fluxYouTubeChannel = Flux.fromIterable(youtubeChannels);

        fluxYouTubeChannel.flatMap(videos -> videos.getAllVideosName().log()).subscribe();
    }

    @Test
    public void flatMapLike() {
        List<Video> videos = MockVideo.generateVideos3();
        YoutubeChannel youtubeChannel = new YoutubeChannel(videos);

        Flux<Integer> videoFlux = youtubeChannel.getAllVideos()
                .flatMap(video -> video.like())
                .map(video -> video.getLikes());

        StepVerifier.create(videoFlux)
                .expectNext(
                        videos.get(0).getLikes() + 1,
                        videos.get(1).getLikes() + 1,
                        videos.get(2).getLikes() + 1).verifyComplete();
    }

    @Test
    public void filterByRatingTest() {

        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());


        youtubeChannel.getVideosByRating(199).subscribe(System.out::println);
    }

    @Test
    public void delayPrintVideosTest() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());

        Flux<String> channel = youtubeChannel
                .getAllVideosName()
                .log()
                .delayElements(
                        Duration.ofSeconds(2));

        StepVerifier.create(channel)
                .expectNext("Nome video 1", "Nome video 2", "Nome video 3")
                .verifyComplete();
    }

    @Test
    public void delayPrintVideosTest2() throws InterruptedException {

        // sem o step verifier a thread principal nao aguarda a emissao dos elementos proposital
        // para aguardar a emissao dos elementos podemos bloquear a thread principal usando o metodo thread sleep do java
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());

        youtubeChannel
                .getAllVideosName()
                .log()
                .delayElements(
                        Duration.ofSeconds(2)).subscribe();

        Thread.sleep(1000);


    }

    @Test
    public void simpleTransform() {
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(num -> num % 2 == 0)
                .transform(squareNumber())
                .subscribe(result -> System.out.println(" = " + result));

        System.out.println("");


        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(num -> num % 2 != 0)
                .transform(squareNumber())
                .subscribe(result -> System.out.println(" = " + result));

    }

    Function<Flux<Integer>, Flux<Integer>> squareNumber() {
        return flux -> flux
                .doOnNext(num -> System.out.print("Square of " + num))
                .map(num -> num * num);
    }

    @Test
    public void transformExample() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());


        youtubeChannel.getAllVideos()
                .transform(transformMethod())
                .subscribe(videoName -> System.out.println("Name Video: " + videoName));


    }

    Function<Flux<Video>, Flux<String>> transformMethod() {
        return flux ->
                flux.filter(video -> video.getLikes() > 1)
                        .map(video -> video.getName())
                        .map(videoName -> videoName.toUpperCase());
    }

    @Test
    public void sideEffectsDoFirst() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().log()
                .doFirst(() -> System.out.println("doFirst()"))
                .subscribe();

    }

    @Test
    public void sideEffectsDoOnSubscribe() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().log()
                .doOnSubscribe((subscription) -> System.out.println("doOnSubscribe()" + subscription))
                .subscribe();

    }

    @Test
    public void sideEffectsDoOnRequest() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().log()
                .doOnRequest((request) -> System.out.println("doOnRequest()" + request))
                .subscribe();

    }

    @Test
    public void sideEffectsDoOnNext() {

        //executar uma acao a cada request

        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().log()
                .doOnNext((item) -> System.out.println("doOnNext()" + item))
                .subscribe();

    }


    @Test
    public void sideEffectsDoOnError() {

        //executar uma acao a cada request

        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().log()
                .doOnError((exception) -> System.out.println("doOnNext()" + exception))
                .subscribe();

    }

    @Test
    public void sideEffectsDoOnCancel() {

        //executar uma acao a cada request

        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().log()
                .doOnCancel(() -> System.out.println("doOnCancel()"))
                .take(2) // tem efeito nos operadores upstream
                .subscribe();

    }


    @Test
    public void sideEffectsDoOnCompletel() {

        //executar uma acao a cada request

        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().log()
                .doOnComplete(() -> System.out.println("doOnComplete()"))
                .subscribe();

    }

    @Test
    public void sideEffectsDoAfterTerminate() {

        //executar uma acao a cada request

        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideos().log()
                .doAfterTerminate(() -> System.out.println("doAfterTerminate()"))
                .subscribe();

    }


    @Test
    public void sideEffectsDoFinally() {

        //executar uma acao a cada request

        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        // a prioridade e sempre upstream no caso a troca do dofinnaly e do after terminate mostra corretamente a execucao dos metodos como sera printado
        youtubeChannel.getAllVideos().log()
                .doAfterTerminate(() -> System.out.println("doAfterTerminate()"))
                .doFinally(signalType -> System.out.println("doFinally() ==>> " + signalType)) // emite o ultimo sinal, no caso como nao ocorreu erro emite o onComplete
                .subscribe();

    }


}
