package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class Community {
    private int duration;
    private List<CommunityListItem> communityPostList;

    public Community() {
        this.duration = 0;
        this.communityPostList = new ArrayList<CommunityListItem>() {};
    }

    public void addDestination(int duration, Destination destination, Accommodation accommodation, Dining dining, String transport) {
        this.communityPostList.add(new CommunityListItem(duration, destination, accommodation, dining, transport));
        this.duration += duration;
    }

    public CommunityListItem getItem (int index) {
        return communityPostList.get(index);
    }

    public int getDuration() {
        return this.duration;
    }

    public List<CommunityListItem> getCommunityPostList() {
        return this.communityPostList;
    }
}

