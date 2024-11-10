package com.example.sprintproject.model;

import java.util.List;

public interface FilterStrategy {
    <T> List<T> filter(List<T> items, String param);
}