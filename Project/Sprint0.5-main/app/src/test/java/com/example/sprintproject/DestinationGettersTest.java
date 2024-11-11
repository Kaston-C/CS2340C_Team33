package com.example.sprintproject;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.sprintproject.model.Destination;

public class DestinationGettersTest {

    private Destination destination;

    @Test
    public void testSetAndGetStartDate() {
        destination = new Destination("", "09/15/2023", "", 0);
        String expectedStartDate = "09/15/2023";
        assertEquals(expectedStartDate, destination.getStartDate());
    }

    @Test
    public void testSetAndGetEndDate() {
        destination = new Destination();
        String expectedEndDate = "09/20/2023";
        destination.setEndDate(expectedEndDate);
        assertEquals(expectedEndDate, destination.getEndDate());
    }

    @Test
    public void testSetAndGetDuration() {
        destination = new Destination();
        int expectedDuration = 5;
        destination.setDuration(expectedDuration);
        assertEquals(expectedDuration, destination.getDuration());
    }
}
