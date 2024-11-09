package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class FilterRoomNum implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        int numRooms = Integer.parseInt(param);
        List<T> filteredList = new ArrayList<>();
        if (items.get(0) instanceof Accommodation) {
            for (T accommodation : items) {
                if (((Accommodation) accommodation).getRoomNumber() == numRooms) {
                    filteredList.add(accommodation);
                }
            }
        }
        return filteredList;
    }
}
