package com.example.sprintproject;

import static org.junit.Assert.*;

import com.example.sprintproject.model.Destination;

import org.junit.Test;

public class DestinationModelTest {
    @Test
    public void calculateDurationInDays() {
        int dur;
        dur = Destination.calculateDurationInDays("10/23/24", "10/25/24");
        assertEquals(dur, 3);
    }

    @Test
    public void calculateEndDate() {
        String end;
        end = Destination.calculateEndDate("10/1/2024", 15);
        assertEquals("10/15/2024", end);
    }

    @Test
    public void calculateStartDate() {
        String end;
        end = Destination.calculateStartDate("10/25/2024", 15);
        assertEquals("10/11/2024", end);
    }
}