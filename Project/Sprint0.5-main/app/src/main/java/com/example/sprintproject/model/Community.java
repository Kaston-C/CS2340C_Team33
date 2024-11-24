package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class Community {
    private int duration;
    private List<CommunityListItem> communityPostList;

    public Community() {
        duration = 0;
        communityPostList = new ArrayList<CommunityListItem>() {};
    }

    public void addDestination(int duration, Destination destination, Accommodation accommodation, Dining dining, String transport) {
        communityPostList.add(new CommunityListItem(duration, destination, accommodation, dining, transport));
        duration += duration;
    }

    public CommunityListItem getItem (int index) {
        return communityPostList.get(index);
    }
}

