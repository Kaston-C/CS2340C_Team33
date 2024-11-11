package com.example.sprintproject;

import static org.junit.Assert.assertEquals;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.FilterRoomType;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class FilterRoomTesting {

    @Test
    public void testFilterWithMultipleRoomType() {

        FilterRoomType filter = new FilterRoomType();
        List<Accommodation> accommodations = new ArrayList<>();

        Accommodation room1 = new Accommodation();
        room1.setRoomType("Single");
        accommodations.add(room1);

        Accommodation room2 = new Accommodation();
        room2.setRoomType("Normal");
        accommodations.add(room2);

        Accommodation room3 = new Accommodation();
        room3.setRoomType("Single");
        accommodations.add(room3);

        List<Accommodation> filtered = filter.filter(accommodations, "Single");

        assertEquals("Single", filtered.get(0).getRoomType());
        assertEquals("Single", filtered.get(1).getRoomType());
    }

    @Test
    public void testNullRoomType() {
        // Arrange
        FilterRoomType filter = new FilterRoomType();
        List<Accommodation> accommodations = new ArrayList<>();

        Accommodation room1 = new Accommodation();
        room1.setRoomType("Standard");
        accommodations.add(room1);

        List<Accommodation> filtered = filter.filter(accommodations, null);

        assertEquals(0, filtered.size());
    }

}
