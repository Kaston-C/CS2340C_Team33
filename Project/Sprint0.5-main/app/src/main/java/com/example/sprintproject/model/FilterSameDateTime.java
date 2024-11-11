package com.example.sprintproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FilterSameDateTime implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        List<T> filteredList = new ArrayList<>();

        if (!items.isEmpty()) {
            if (items.get(0) instanceof Dining) {
                LocalDateTime dateTime = LocalDateTime.parse(param, dateFormatter);
                for (T item : items) {
                    Dining dining = (Dining) item;
                    if (dining.getReservationDateTime().equals(dateTime)) {
                        filteredList.add(item);
                    }
                }
            } else if (items.get(0) instanceof Accommodation) {
                for (T item : items) {
                    Accommodation accommodation = (Accommodation) item;
                    LocalDateTime dateTime = LocalDateTime.parse(param + " 12:00", dateFormatter);

                    String checkInDateStr = accommodation.getCheckInDate();
                    LocalDateTime checkInDate
                            = LocalDateTime.parse(checkInDateStr + " 12:00", dateFormatter);

                    if (checkInDate.equals(dateTime)) {
                        filteredList.add(item);
                    }
                }
            }
        }

        return filteredList;
    }
}
