package com.example.sprintproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FilterBeforeDateTime implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        List<T> filteredList = new ArrayList<>();

        if (!items.isEmpty()) {
            if (items.get(0) instanceof Dining) {
                diningHelper(items, filteredList, param);
            } else if (items.get(0) instanceof Accommodation) {
                accommodationHelper(items, filteredList, param);
            }
        }

        return filteredList;
    }

    private <T> void diningHelper(List<T> items, List<T> filteredList, String param) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(param, dateFormatter);
        for (T item : items) {
            Dining dining = (Dining) item;
            if (dining.getReservationDateTime().isBefore(dateTime)) {
                filteredList.add(item);
            }
        }
    }

    private <T> void accommodationHelper(List<T> items, List<T> filteredList, String param) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        for (T item : items) {
            Accommodation accommodation = (Accommodation) item;
            LocalDateTime dateTime = LocalDateTime.parse(param + " 12:00", dateFormatter);

            String checkInDateStr = accommodation.getCheckInDate();
            LocalDateTime checkInDate
                    = LocalDateTime.parse(checkInDateStr + " 12:00", dateFormatter);

            if (checkInDate.isBefore(dateTime)) {
                filteredList.add(item);
            }
        }
    }
}
