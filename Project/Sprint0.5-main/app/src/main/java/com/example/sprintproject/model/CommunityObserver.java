package com.example.sprintproject.model;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class CommunityObserver implements ObserverStrategy {
    public void update(List<TravelPost> travelPostList, DataSnapshot snapshot) {
        travelPostList.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            TravelPost travelPost = dataSnapshot.getValue(TravelPost.class);
            if (travelPost != null) {
                travelPostList.add(travelPost);
            }
        }

    }

}
