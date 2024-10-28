package com.example.sprintproject;

import static org.junit.Assert.*;

import com.example.sprintproject.model.Destination;

import org.junit.Test;

//Srihitha-Testing to make sure that invalid outputs result in null and do not give wrong/negative results

public class VisualizationInvalidTesting {

    @Test
    //Testing the invalidity of START to make sure that pie chart displays correct outputs
    public void calculateInvaliditySTARTDates() {
        assertNull(Destination.calculateStartDate("invalid_date", 20));
    }

    @Test
    //Testing the invalidity of the end date, so that pie chart displays correct data
    public void calculateDateInvalidityEND() {
        // Testing with an invalid start date should return null
        String endDate;
        endDate = Destination.calculateEndDate("invalid_date", 45);
        assertNull(endDate);
    }


}

