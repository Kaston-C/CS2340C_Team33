package com.example.sprintproject;

import static org.junit.Assert.assertEquals;
import com.example.sprintproject.model.TravelPost;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;


public class TravelTests {
  
    @Test
    public void testingTravelPostDetails() {

        TravelPost travelPost = new TravelPost("Spain", "2024-11-01", "2024-11-10", "Hotel", "Coffee Shop", "Train", "Fun!");
        assertEquals("Spain", travelPost.getDestination());
        assertEquals("2024-11-01", travelPost.getStartDate());
        assertEquals("2024-11-10", travelPost.getEndDate());
        assertEquals("Hotel", travelPost.getAccommodation());
        assertEquals("Coffee Shop", travelPost.getDining());
        assertEquals("Train", travelPost.getTransportation());
        assertEquals("Fun!", travelPost.getNotes());
    }


    @Test
    public void testingTravelListAdding() {

        List<TravelPost> travel = new ArrayList<>();
        assertEquals(0, travel.size());

        TravelPost travels = new TravelPost("Spain", "2024-09-01", "2024-09-10", "Hotal", "Donut Shop", "Bus", "Fun Trip!");
        travel.add(travels);

        assertEquals(1, travel.size());
    }
}
