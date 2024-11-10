package com.example.sprintproject.model;

import java.time.LocalDateTime;

public class Dining {
    private String name;
    private String location;
    private String website;
    private LocalDateTime reservationDateTime;

    public Dining(String name, String location, String website, LocalDateTime reservationDateTime) {
        this.name = name;
        this.location = location;
        this.website = website;
        this.reservationDateTime = reservationDateTime;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }
}
