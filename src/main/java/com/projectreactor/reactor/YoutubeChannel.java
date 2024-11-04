package com.projectreactor.reactor;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class YoutubeChannel {

    private List<Video> videos;


    public YoutubeChannel() {
        this.videos = new ArrayList<>();
    }

    public YoutubeChannel(List<Video> videos) {
        this.videos = videos;
    }


    public void addVideo(Video video) {
        this.videos.add(video);
    }

    public Flux<Video> getAllVideos(int number) {
        return Flux.fromIterable(videos).log().take(number);
    }

    public Flux<Video> getAllVideos() {
        return Flux.fromIterable(videos);
    }

    public Flux<Integer> getDescriptionSize() {
        return getAllVideos().map(video -> video.getDescription().length());
    }

    public Flux<String> getAllVideosName() {
        return getAllVideos().map(Video::getName);
    }

    public Flux<Video> getVideosByRating(Integer rate) {
        return getAllVideos().filter(video -> video.getLikes() > rate);
    }

}
