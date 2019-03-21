package com.example.test.dao;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class Event implements Parcelable {
    private String date;
    private String eventName;
    private String category;
    private String venueInfo;
    private String id;
    private String fav;
    private String addTime;
    public Event() {

    }
    public Event(String date, String eventName, String category, String venueInfo, String id, String fav) {
        this.date = date;
        this.eventName = eventName;
        this.category = category;
        this.venueInfo = venueInfo;
        this.id = id;
        this.fav = fav;
    }
    public Event(Parcel in) {
        super();
        readFromParcel(in);
    }
    public void readFromParcel(Parcel in) {
        date = in.readString();
        eventName = in.readString();
        category = in.readString();
        venueInfo = in.readString();
        id = in.readString();
        fav = in.readString();
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public static Comparator<Event> addTimeComparator = new Comparator<Event>() {
        public int compare(Event u1, Event u2) {
            String eventName1 = u1.getAddTime().toUpperCase();
            String eventName2 = u2.getAddTime().toUpperCase();
            return eventName1.compareTo(eventName2);
        }};

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            // return new Event(in.readString(), in.readString(),in.readString(),in.readString(),in.readString(),in.readString());
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.eventName);
        dest.writeString(this.category);
        dest.writeString(this.venueInfo);
        dest.writeString(this.id);
        dest.writeString(this.fav);
    }

    public String getDate() {
        return date;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCategory() {
        return category;
    }

    public String getVenueInfo() {
        return venueInfo;
    }

    public String getId() {
        return id;
    }

    public String getFav() {
        return fav;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setVenueInfo(String venueInfo) {
        this.venueInfo = venueInfo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }
}


