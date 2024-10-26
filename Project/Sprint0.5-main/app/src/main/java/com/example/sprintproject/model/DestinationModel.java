package com.example.sprintproject.model;

import java.util.Calendar;

public class DestinationModel {
    private String location;
    private Calendar startDate;
    private Calendar endDate;
    private int duration;

    public DestinationModel() {}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int calculateDurationInDays() {
        if (startDate != null && endDate != null) {
            return (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / (60 * 1000 * 60 * 24));
        }
        return duration;
    }
}
