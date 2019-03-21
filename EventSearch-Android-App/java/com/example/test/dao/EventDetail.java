package com.example.test.dao;

public class EventDetail {
    private String artist;
    private String venue;
    private String time;
    private String category;
    private String lowPrice;
    private String highPrice;
    private String ticketStatus;
    private String buyTicketAt;
    private String seatMap;
    private String eventId;
    public EventDetail () {

    }

    public EventDetail(String artist,String venue, String time, String category, String lowPrice, String highPrice, String
            ticketStatus, String buyTicketAt, String seatMap, String eventId) {
        this.artist = artist;
        this.venue = venue;
        this.time = time;
        this.category = category;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
        this.ticketStatus = ticketStatus;
        this.buyTicketAt = buyTicketAt;
        this.seatMap = seatMap;
        this.eventId = eventId;
}

    public String getArtist() {
        return artist;
    }

    public String getVenue() {
        return venue;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getBuyTicketAt() {
        return buyTicketAt;
    }

    public String getSeatMap() {
        return seatMap;
    }

    public String getEventId() {
        return eventId;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public void setBuyTicketAt(String buyTicketAt) {
        this.buyTicketAt = buyTicketAt;
    }

    public void setSeatMap(String seatMap) {
        this.seatMap = seatMap;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}


