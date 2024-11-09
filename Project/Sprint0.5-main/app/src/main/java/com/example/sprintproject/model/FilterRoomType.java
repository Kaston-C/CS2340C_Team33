package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class FilterRoomType implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        List<T> filteredList = new ArrayList<>();
        if (items.get(0) instanceof Accommodation) {
            for (T accommodation : items) {
                if (((Accommodation) accommodation).getRoomType().equals(param)) {
                    filteredList.add(accommodation);
                }
            }
        }
        return filteredList;
    }
}
