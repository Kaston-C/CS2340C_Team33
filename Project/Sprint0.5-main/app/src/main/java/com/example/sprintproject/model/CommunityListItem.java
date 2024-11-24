package com.example.sprintproject.model;

public class CommunityListItem {
    private int duration;
    private Destination destination;
    private Accommodation accommodation;
    private Dining dining;
    private String transport;

    public CommunityListItem (int duration, Destination destination, Accommodation accommodation, Dining dining, String transport) {
        this.duration = duration;
        this.destination = destination;
        this.accommodation = accommodation;
        this.dining = dining;
        this.transport = transport;
    }

    public int getDuration() {return duration;}

    public Destination getDestination() {return destination;}

    public Accommodation getAccommodation() {return accommodation;}

    public Dining getDining() {return dining;}

    public String getTransport() {return transport;}
}
