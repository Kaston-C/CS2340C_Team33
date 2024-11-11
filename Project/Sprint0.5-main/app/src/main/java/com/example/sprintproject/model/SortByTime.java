package com.example.sprintproject.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortByTime implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        if (items.get(0) instanceof Dining) {
            List<Dining> diningList = items.stream()
                    .filter(Dining.class::isInstance)
                    .map(Dining.class::cast).sorted(new Comparator<Dining>() {
                        @Override
                        public int compare(Dining e1, Dining e2) {
                            LocalDateTime date1 = e1.getReservationDateTime();
                            LocalDateTime date2 = e2.getReservationDateTime();
                            return date1.compareTo(date2);
                        }
                    }).collect(Collectors.toList());
            return (List<T>) diningList;
        } else if (items.get(0) instanceof Accommodation) {
            if (param.equals("checkIn")) {
                List<Accommodation> accommodationList = items.stream()
                        .filter(Accommodation.class::isInstance)
                        .map(Accommodation.class::cast).sorted(new Comparator<Accommodation>() {
                            @Override
                            public int compare(Accommodation e1, Accommodation e2) {
                                LocalDateTime date1 = e1.getCheckInDateTime();
                                LocalDateTime date2 = e2.getCheckInDateTime();
                                return date1.compareTo(date2);
                            }
                        }).collect(Collectors.toList());
                return (List<T>) accommodationList;
            } else {
                List<Accommodation> accommodationList = items.stream()
                        .filter(Accommodation.class::isInstance)
                        .map(Accommodation.class::cast).sorted(new Comparator<Accommodation>() {
                            @Override
                            public int compare(Accommodation e1, Accommodation e2) {
                                LocalDateTime date1 = e1.getCheckOutDateTime();
                                LocalDateTime date2 = e2.getCheckOutDateTime();
                                return date1.compareTo(date2);
                            }
                        }).collect(Collectors.toList());
                return (List<T>) accommodationList;
            }
        }
        return new ArrayList<>();
    }
}
