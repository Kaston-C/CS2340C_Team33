package com.example.sprintproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dining {
    private String id;
    private String date;
    private String time;
    private String location;
    private String website;

    public Dining() {
    }

    public Dining(String id, String date, String time, String location, String website) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.location = location;
        this.website = website;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public LocalDateTime getReservationDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        return LocalDateTime.parse(date + " " + time, dateFormatter);
    }
}
