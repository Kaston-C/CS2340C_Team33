package com.example.sprintproject.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FilterSameDateTime implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        LocalDateTime dateTime = LocalDateTime.parse(param);
        List<T> filteredList = new ArrayList<>();
        if (items.get(0) instanceof Dining) {
            for (T dining : items) {
                if (((Dining) dining).getReservationDateTime().equals(dateTime)) {
                    filteredList.add(dining);
                }
            }
        } else if (items.get(0) instanceof Accommodation) {
            for (T accommodation : items) {
                if (((Accommodation) accommodation).getCheckInDate().equals(dateTime)) {
                    filteredList.add(accommodation);
                }
            }
        }
        return filteredList;
    }
}