package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class FilterRoomNum implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        int numRooms = Integer.parseInt(param);
        List<T> filteredList = new ArrayList<>();

        if (!items.isEmpty() && items.get(0) instanceof Accommodation) {
            for (T item : items) {
                Accommodation accommodation = (Accommodation) item;
                if (accommodation.getNumberOfRooms() == numRooms) {
                    filteredList.add(item);
                }
            }
        }

        return filteredList;
    }
}
