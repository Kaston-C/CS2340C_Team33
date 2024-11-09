package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class FilterLocation implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        List<T> filteredList = new ArrayList<>();
        if (items.get(0) instanceof Dining) {
            for (T dining : items) {
                if (((Dining) dining).getLocation().equals(param)) {
                    filteredList.add(dining);
                }
            }
        } else if (items.get(0) instanceof Accommodation) {
            for (T accommodation : items) {
                if (((Accommodation) accommodation).getLocation().equals(param)) {
                    filteredList.add(accommodation);
                }
            }
        }
        return filteredList;
    }
}
