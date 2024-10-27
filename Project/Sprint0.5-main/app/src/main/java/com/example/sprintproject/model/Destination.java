package com.example.sprintproject.model;

public class Destination {
    private String id;
    private String location;
    private String startDate;
    private String endDate;
    private int duration;

    public Destination() {}

    public Destination(String id, String location, String startDate, String endDate, int duration) {
        this.id = id;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getLocation() { return location; }

    public String getStartDate() { return startDate; }

    public String getEndDate() { return endDate; }
}
