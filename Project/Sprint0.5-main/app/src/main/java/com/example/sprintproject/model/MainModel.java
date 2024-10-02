package com.example.sprintproject.model;

public class MainModel {
    private String welcomeMessage;

    public MainModel() {
        this.welcomeMessage = "Welcome to WanderSync!";
    }

    public String getWelcomeMessage() {
        return this.welcomeMessage;
    }

    public void setWelcomeMessage(String message) {
        this.welcomeMessage = message;
    }
}
