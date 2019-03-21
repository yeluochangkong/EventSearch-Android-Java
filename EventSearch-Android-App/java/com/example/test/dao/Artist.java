package com.example.test.dao;

import java.util.List;

public class Artist {
    private String name;
    private String followers;
    private String popularity;
    private String checkAt;
    private List<String> photos;

    public Artist() {

    }
    public Artist(String name, String followers, String popularity, String checkAt) {
        this.name = name;
        this.followers = followers;
        this.popularity = popularity;
        this.checkAt = checkAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setCheckAt(String checkAt) {
        this.checkAt = checkAt;
    }

    public Artist(List<String> photos) {
        this.photos = photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getName() {
        return name;
    }

    public String getFollowers() {
        return followers;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getCheckAt() {
        return checkAt;
    }

    public List<String> getPhotos() {
        return photos;
    }
}


