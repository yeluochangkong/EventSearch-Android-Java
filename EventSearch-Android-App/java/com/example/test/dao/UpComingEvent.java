package com.example.test.dao;

import java.util.Comparator;

public class UpComingEvent {
    private String displayName;
    private String uri;
    private String artist;
    private String time;
    private String type;

    public UpComingEvent() {
    }

    public UpComingEvent(String displayName, String uri, String artist, String time, String type) {
        this.displayName = displayName;
        this.uri = uri;
        this.artist = artist;
        this.time = time;
        this.type = type;
    }

    public static Comparator<UpComingEvent> eventNameComparator = new Comparator<UpComingEvent>() {
        public int compare(UpComingEvent u1, UpComingEvent u2) {
            String eventName1 = u1.getDisplayName().toUpperCase();
            String eventName2 = u2.getDisplayName().toUpperCase();
            return eventName1.compareTo(eventName2);
        }};
    public static Comparator<UpComingEvent> timeComparator = new Comparator<UpComingEvent>() {
        public int compare(UpComingEvent u1, UpComingEvent u2) {
            String time1 = u1.getTime().toUpperCase();
            String time2 = u2.getTime().toUpperCase();
            return time1.compareTo(time2);
        }};
    public static Comparator<UpComingEvent> artistComparator = new Comparator<UpComingEvent>() {
        public int compare(UpComingEvent u1, UpComingEvent u2) {
            String artist1 = u1.getArtist().toUpperCase();
            String artist2 = u2.getArtist().toUpperCase();
            return artist1.compareTo(artist2);
        }};
    public static Comparator<UpComingEvent> typeComparator = new Comparator<UpComingEvent>() {
        public int compare(UpComingEvent u1, UpComingEvent u2) {
            String type1 = u1.getType().toUpperCase();
            String type2 = u2.getType().toUpperCase();
            return type1.compareTo(type2);
        }};


    public String getDisplayName() {
        return displayName;
    }

    public String getUri() {
        return uri;
    }

    public String getArtist() {
        return artist;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }
}

