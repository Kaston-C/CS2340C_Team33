package com.example.sprintproject.model;

public class TravelPost {
    private String username;
    private String destination;
    private String startDate;
    private String endDate;
    private String accommodation;
    private String dining;
    private String transportation;
    private String notes;

    public TravelPost() {
    }

    public TravelPost(String username, String destination, String startDate, String endDate,
                      String accommodation, String dining, String transportation, String notes) {
        this.username = username;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accommodation = accommodation;
        this.dining = dining;
        this.transportation = transportation;
        this.notes = notes;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getAccommodation() { return accommodation; }
    public void setAccommodation(String accommodation) { this.accommodation = accommodation; }
    public String getDining() { return dining; }
    public void setDining(String dining) { this.dining = dining; }
    public String getTransportation() { return transportation; }
    public void setTransportation(String transportation) { this.transportation = transportation; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

