package com.example.sprintproject.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FilterAfterDateTime implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        LocalDateTime dateTime = LocalDateTime.parse(param);
        List<T> filteredList = new ArrayList<>();
        if (items.get(0) instanceof Dining) {
            for (T dining : items) {
                if (((Dining) dining).getReservationDateTime().isAfter(dateTime)) {
                    filteredList.add(dining);
                }
            }
        } else if (items.get(0) instanceof Accommodation) {
            for (T accommodation : items) {
                if (((Accommodation) accommodation).getCheckIn().isAfter(dateTime)) {
                    filteredList.add(accommodation);
                }
            }
        }
        return filteredList;
    }
}
