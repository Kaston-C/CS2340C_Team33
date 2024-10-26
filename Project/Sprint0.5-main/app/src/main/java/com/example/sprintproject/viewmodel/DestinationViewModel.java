package com.example.sprintproject.viewmodel;

import android.widget.EditText;

import com.example.sprintproject.model.MainModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class DestinationViewModel {

    public static DatabaseReference firebaseConnect() {
        return MainModel.firebaseConnect("destinations");
    }
    public static FirebaseAuth firebaseAuthorization() {
        return MainModel.getFirebaseAuthorization();
    }

    public static String getInputStart(EditText startInput) {
        return MainModel.getInputStart(startInput);
    }

    public static String getInputEnd(EditText endInput) {
        return MainModel.getInputEnd(endInput);
    }

    public static String getInputDestination(EditText destinationInput) {
        return MainModel.getInputDestination(destinationInput);
    }



}
