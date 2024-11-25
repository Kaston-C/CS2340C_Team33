package com.example.sprintproject.model;

public class CommunityListItem {
    private int duration;
    private Destination destination;
    private Accommodation accommodation;
    private Dining dining;
    private String transport;

    public CommunityListItem(int dur, Destination d, Accommodation acc, Dining din, String tran) {
        this.duration = dur;
        this.destination = d;
        this.accommodation = acc;
        this.dining = din;
        this.transport = tran;
    }

    public int getDuration() {
        return duration;
    }

    public Destination getDestination() {
        return destination;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public Dining getDining() {
        return dining;
    }

    public String getTransport() {
        return transport;
    }
}
