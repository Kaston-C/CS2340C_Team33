package com.example.sprintproject;

import com.example.sprintproject.model.Accommodation;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccommodationTest {

    @Test
    public void testValidDate() {
        assertTrue(Accommodation.isValidDate("12/31/2023"));
        assertFalse(Accommodation.isValidDate("31/12/2023"));
        assertFalse(Accommodation.isValidDate("2023/12/31"));
        assertFalse(Accommodation.isValidDate("12-31-2023"));
        assertFalse(Accommodation.isValidDate("invalid date"));
    }

    @Test
    public void testCheckOutDateValid() {
        assertTrue(Accommodation.isCheckOutDateValid("12/31/2023", "01/01/2024"));
        assertTrue(Accommodation.isCheckOutDateValid("12/31/2023", "12/31/2023"));
        assertFalse(Accommodation.isCheckOutDateValid("12/31/2023", "12/30/2023"));
        assertFalse(Accommodation.isCheckOutDateValid("12/31/2023", "invalid date"));
        assertFalse(Accommodation.isCheckOutDateValid("invalid date", "12/31/2023"));
    }
}
