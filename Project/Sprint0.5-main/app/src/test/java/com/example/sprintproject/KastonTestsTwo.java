package com.example.sprintproject;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.FilterAfterDateTime;
import com.example.sprintproject.model.FilterBeforeDateTime;
import com.example.sprintproject.model.FilterLocation;
import com.example.sprintproject.model.FilterRoomNum;
import com.example.sprintproject.model.FilterRoomType;
import com.example.sprintproject.model.FilterSameDateTime;
import com.example.sprintproject.model.FilterStrategy;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class KastonTestsTwo {
    List<Dining> diningList;
    List<Accommodation> accommodationList;
    FilterStrategy filterStrategy;

    @Before
    public void setup() {
        diningList = new ArrayList<Dining>();
        for (int i = 1; i < 9; i++) {
            Dining dining = new Dining("item" + i, "11/0" + i + "/2024", "12:0" + i, "item" + i, "item" + i);
            diningList.add(dining);
        }

        accommodationList = new ArrayList<Accommodation>();
        for (int i = 1; i < 9; i++) {
            Accommodation accommodation;
            if (i < 5) {
                accommodation = new Accommodation("item" + i, "11/0" + i + "/2024", "11/0" + (i + 1) + "/2024", "item" + i, i, "king", "item" + i);
            } else {
                accommodation = new Accommodation("item" + i, "11/0" + i + "/2024", "11/0" + (i + 1) + "/2024", "item" + i, i, "queen", "item" + i);
            }
            accommodationList.add(accommodation);
        }
    }

    @Test
    public void testFilterDining() {
        filterStrategy = new FilterBeforeDateTime();
        List<Dining> diningToCheck = filterStrategy.filter(diningList, "11/05/2024 12:05");
        assertArrayEquals(diningToCheck.toArray(), diningList.subList(0,4).toArray());

        filterStrategy = new FilterAfterDateTime();
        diningToCheck = filterStrategy.filter(diningList, "11/05/2024 12:05");
        assertArrayEquals(diningToCheck.toArray(), diningList.subList(5, 8).toArray());

        filterStrategy = new FilterSameDateTime();
        diningToCheck = filterStrategy.filter(diningList, "11/05/2024 12:05");
        assertArrayEquals(diningToCheck.toArray(), diningList.subList(4, 5).toArray());
    }

    @Test
    public void testFilterAccommodation() {
        filterStrategy = new FilterBeforeDateTime();
        List<Accommodation> accommodationToCheck = filterStrategy.filter(accommodationList, "11/05/2024");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(0,4).toArray());

        filterStrategy = new FilterAfterDateTime();
        accommodationToCheck = filterStrategy.filter(accommodationList, "11/05/2024");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(5, 8).toArray());

        filterStrategy = new FilterSameDateTime();
        accommodationToCheck = filterStrategy.filter(accommodationList, "11/05/2024");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(4, 5).toArray());
    }

    @Test
    public void testFilterLocation() {
        filterStrategy = new FilterLocation();
        List<Dining> diningToCheck = filterStrategy.filter(diningList, "item1");
        assertArrayEquals(diningToCheck.toArray(), diningList.subList(0, 1).toArray());
        List<Accommodation> accommodationToCheck = filterStrategy.filter(accommodationList, "item3");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(2, 3).toArray());
    }

    @Test
    public void testFilterRoomNum() {
        filterStrategy = new FilterRoomNum();
        List<Accommodation> accommodationToCheck = filterStrategy.filter(accommodationList, "1");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(0, 1).toArray());
    }


    @Test
    public void testFilterRoomType() {
        filterStrategy = new FilterRoomType();
        List<Accommodation> accommodationToCheck = filterStrategy.filter(accommodationList, "item3");
        accommodationToCheck = filterStrategy.filter(accommodationList, "king");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(0, 4).toArray());
    }
}
