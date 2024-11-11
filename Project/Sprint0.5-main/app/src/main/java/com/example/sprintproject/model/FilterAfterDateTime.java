package com.example.sprintproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FilterAfterDateTime implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        List<T> filteredList = new ArrayList<>();

        if (items.get(0) instanceof Dining) {
            LocalDateTime dateTime = LocalDateTime.parse(param, dateFormatter);
            for (T dining : items) {
                if (((Dining) dining).getReservationDateTime().isAfter(dateTime)) {
                    filteredList.add(dining);
                }
            }
        } else if (items.get(0) instanceof Accommodation) {
            for (T accommodation : items) {
                LocalDateTime dateTime = LocalDateTime.parse(param + " 12:00", dateFormatter);

                String checkInDateStr = ((Accommodation) accommodation).getCheckInDate();
                LocalDateTime checkInDate = LocalDateTime.parse(
                        checkInDateStr + " 12:00", dateFormatter);
                if (checkInDate.isAfter(dateTime)) {
                    filteredList.add(accommodation);
                }
            }
        }
        return filteredList;
    }
}
