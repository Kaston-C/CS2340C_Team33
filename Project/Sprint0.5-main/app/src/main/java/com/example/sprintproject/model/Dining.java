package com.example.sprintproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dining {
    private String date;
    private String time;
    private String location;
    private String website;

    public Dining() {
    }

    public Dining(String date, String time, String location, String website) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.website = website;
    }
    public Dining(String location, String website, LocalDateTime dateTime) {
        this.date = dateTime.toLocalDate().toString();
        this.time = dateTime.toLocalTime().toString();
        this.location = location;
        this.website = website;
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

    public static boolean isValidDateTimeFormat(String date, String time) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public LocalDateTime getReservationDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        return LocalDateTime.parse(date + " " + time, dateFormatter);
    }
}
