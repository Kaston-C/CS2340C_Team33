package com.example.sprintproject.model;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface ObserverStrategy {
    public void update(List<TravelPost> travelPostList, DataSnapshot snapshot);

}
