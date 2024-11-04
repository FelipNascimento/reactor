package com.projectreactor.reactor;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Data
@Builder
@Getter
@Setter
public class Video {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    private Integer likes;

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    private Integer views;

    public Video(String name, String description, Integer likes, Integer views) {
        this.name = name;
        this.description = description;
        this.likes = likes;
        this.views = views;
    }

    public Mono<Video> like() {
        this.likes++;
        return Mono.just(this);
    }


}
