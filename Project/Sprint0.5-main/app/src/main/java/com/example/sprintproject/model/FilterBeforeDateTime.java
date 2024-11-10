package com.example.sprintproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FilterBeforeDateTime implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        LocalDateTime dateTime = LocalDateTime.parse(param);

        List<T> filteredList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        if (!items.isEmpty()) {
            if (items.get(0) instanceof Dining) {
                for (T item : items) {
                    Dining dining = (Dining) item;
                    if (dining.getReservationDateTime().isBefore(dateTime)) {
                        filteredList.add(item);
                    }
                }
            } else if (items.get(0) instanceof Accommodation) {
                for (T item : items) {
                    Accommodation accommodation = (Accommodation) item;
                    String checkInDateStr = accommodation.getCheckInDate();
                    LocalDateTime checkInDate = LocalDateTime.parse(checkInDateStr + "T00:00:00", formatter);

                    if (checkInDate.isBefore(dateTime)) {
                        filteredList.add(item);
                    }
                }
            }
        }

        return filteredList;
    }
}
