package com.example.sprintproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.sprintproject.model.Dining;

import org.junit.Test;


public class DiningTest {
    @Test
    public void testValidDate() {
        assertTrue(Dining.isValidDateTimeFormat("12/31/2023", "10:30"));
        assertFalse(Dining.isValidDateTimeFormat("31/12/2023", "10:30"));
        assertFalse(Dining.isValidDateTimeFormat("2023/12/31", "10:30"));
        assertFalse(Dining.isValidDateTimeFormat("12-31-2023", "10:30"));
        assertFalse(Dining.isValidDateTimeFormat("invalid date", "10:30"));
    }

    @Test
    public void testCheckOutDateValid() {
        assertTrue(Dining.isValidDateTimeFormat("12/31/2023", "10:30"));
        assertTrue(Dining.isValidDateTimeFormat("12/31/2023", "10:30"));
        assertFalse(Dining.isValidDateTimeFormat("12/31/2023", "1012231"));
        assertFalse(Dining.isValidDateTimeFormat("234234", "10;30"));
        assertFalse(Dining.isValidDateTimeFormat("invalid date", "10:30"));
    }
}
