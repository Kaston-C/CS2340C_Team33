package com.example.sprintproject.model;

public class User {
    private String username;
    private String trip;
    private String startVacation;
    private String endVacation;
    private int vacationDuration;

    public User() { }

    public User(String username, String trip, String startVacation,
                String endVacation, int vacationDuration) {
        this.username = username;
        this.trip = trip;
        this.startVacation = startVacation;
        this.endVacation = endVacation;
        this.vacationDuration = vacationDuration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getStartVacation() {
        return startVacation;
    }

    public void setStartVacation(String startVacation) {
        this.startVacation = startVacation;
    }

    public String getEndVacation() {
        return endVacation;
    }

    public void setEndVacation(String endVacation) {
        this.endVacation = endVacation;
    }

    public int getVacationDuration() {
        return vacationDuration;
    }

    public void setVacationDuration(int vacationDuration) {
        this.vacationDuration = vacationDuration;
    }
}
