package com.example.sprintproject;

import static org.junit.Assert.assertEquals;

import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.MainModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.junit.Test;

public class HawthorneTest {
    @Test
    public void setWelcomeMessageCheck() {
        String check = "check";
        MainModel testMain = new MainModel();
        testMain.setWelcomeMessage(check);

        assertEquals(testMain.getWelcomeMessage(),check);

    }

    @Test
    public void calculateVacationTimeCheck() {
        Destination trip = new Destination("dubai", "tuesday", "wednesday", 1);

        assertEquals(trip.getDuration(), 1);
    }
}
