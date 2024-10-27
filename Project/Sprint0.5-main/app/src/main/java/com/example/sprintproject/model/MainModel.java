package com.example.sprintproject.model;

import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public static DatabaseReference firebaseConnect(String databaseName) {
        System.out.println(databaseName);
        return FirebaseDatabase.getInstance().getReference(databaseName);
    }

    public static FirebaseAuth getFirebaseAuthorization() {
        return FirebaseAuth.getInstance();
    }

    public static String getInputUsername(EditText usernameInput) {
        String username = usernameInput.getText().toString();
        if (!username.contains("@")) {
            username += "@wandersync.com";
        }
        return username;
    }

    public static String getInputPassword(EditText passwordInput) {
        return passwordInput.getText().toString();
    }

    public static String getInputStart(EditText startInput) {
        return startInput.getText().toString();
    }

    public static String getInputEnd(EditText endInput) {
        return endInput.getText().toString();
    }

    public static String getInputDestination(EditText destinationInput) {
        return destinationInput.getText().toString();
    }
}
