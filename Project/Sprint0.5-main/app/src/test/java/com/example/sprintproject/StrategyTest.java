package com.example.sprintproject;

import static org.junit.Assert.assertArrayEquals;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StrategyTest {
    List<Dining> diningList;
    List<Accommodation> accommodationList;
    FilterStrategy filterStrategy;

    @Before
    public void setup() {
        diningList = new ArrayList<Dining>();
        for (int i = 1; i < 9; i++) {
            LocalDateTime localDateTime = LocalDateTime.parse("2007-12-0" + i+ "T10:15:30");
            Dining dining = new Dining("item" + i, "item" + i, "item" + i, localDateTime);
            diningList.add(dining);
        }

        accommodationList = new ArrayList<Accommodation>();
        for (int i = 1; i < 9; i++) {
            LocalDateTime startDateTime = LocalDateTime.parse("2007-12-0" + i + "T10:15:30");
            LocalDateTime endDateTime = LocalDateTime.parse("2007-12-0" + (i + 1) + "T10:15:30");
            Accommodation accommodation;
            if (i < 5) {
                accommodation = new Accommodation("item" + i, "item" + i, "item" + i, startDateTime, endDateTime, "king", i);
            } else {
                accommodation = new Accommodation("item" + i, "item" + i, "item" + i, startDateTime, endDateTime, "queen", i);
            }
            accommodationList.add(accommodation);
        }
    }

    @Test
    public void testFilterDate() {
        // Dining
        filterStrategy = new FilterBeforeDateTime();
        List<Dining> diningToCheck = filterStrategy.filter(diningList, "2007-12-05T10:15:30");
        assertArrayEquals(diningToCheck.toArray(), diningList.subList(0,4).toArray());

        filterStrategy = new FilterAfterDateTime();
        diningToCheck = filterStrategy.filter(diningList, "2007-12-05T10:15:30");
        assertArrayEquals(diningToCheck.toArray(), diningList.subList(5, 8).toArray());

        filterStrategy = new FilterSameDateTime();
        diningToCheck = filterStrategy.filter(diningList, "2007-12-05T10:15:30");
        assertArrayEquals(diningToCheck.toArray(), diningList.subList(4, 5).toArray());

        // Accommodation
        filterStrategy = new FilterBeforeDateTime();
        List<Accommodation> accommodationToCheck = filterStrategy.filter(accommodationList, "2007-12-05T10:15:30");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(0,4).toArray());

        filterStrategy = new FilterAfterDateTime();
        accommodationToCheck = filterStrategy.filter(accommodationList, "2007-12-05T10:15:30");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(5, 8).toArray());

        filterStrategy = new FilterSameDateTime();
        accommodationToCheck = filterStrategy.filter(accommodationList, "2007-12-05T10:15:30");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(4, 5).toArray());
    }

    @Test
    public void testFilterOther() {
        filterStrategy = new FilterLocation();
        List<Dining> diningToCheck = filterStrategy.filter(diningList, "item1");
        assertArrayEquals(diningToCheck.toArray(), diningList.subList(0, 1).toArray());
        List<Accommodation> accommodationToCheck = filterStrategy.filter(accommodationList, "item3");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(2, 3).toArray());

        filterStrategy = new FilterRoomType();
        accommodationToCheck = filterStrategy.filter(accommodationList, "king");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(0, 4).toArray());

        filterStrategy = new FilterRoomNum();
        accommodationToCheck = filterStrategy.filter(accommodationList, "1");
        assertArrayEquals(accommodationToCheck.toArray(), accommodationList.subList(0, 1).toArray());
    }
}