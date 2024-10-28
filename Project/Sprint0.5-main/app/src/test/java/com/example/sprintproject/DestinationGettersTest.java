// DestinationTest.java
package com.example.sprintproject.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class DestinationGettersTest {

    private Destination destination;

    @Test
    public void testSetAndGetId() {
        destination = new Destination();
        String expectedId = "D12345";
        destination.setId(expectedId);
        assertEquals(expectedId, destination.getId());
    }

    @Test
    public void testSetAndGetStartDate() {
        destination = new Destination();
        String expectedStartDate = "09/15/2023";
        destination.setStartDate(expectedStartDate);
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
