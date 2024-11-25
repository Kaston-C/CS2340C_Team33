package com.example.sprintproject.model;

public class CurrentTrip {
    private static CurrentTrip instance;
    private String currentTripId;
    private String currentTripName;

    private CurrentTrip() { }

    public static synchronized CurrentTrip getInstance() {
        if (instance == null) {
            instance = new CurrentTrip();
        }
        return instance;
    }

    public String getCurrentTripId() {
        return currentTripId;
    }

    public void setCurrentTripId(String currentTripId) {
        this.currentTripId = currentTripId;
    }

    public String getCurrentTripName() {
        return currentTripName;
    }

    public void setCurrentTripName(String currentTripName) {
        this.currentTripName = currentTripName;
    }
}