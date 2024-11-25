package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class Community {
    private int duration;
    private List<CommunityListItem> communityPostList;

    public Community() {
        this.duration = 0;
        this.communityPostList = new ArrayList<CommunityListItem>() { };
    }

    public void addDestination(int dur, Destination d, Accommodation acc, Dining din, String tran) {
        this.communityPostList.add(new CommunityListItem(dur, d, acc, din, tran));
        this.duration += dur;
    }

    public CommunityListItem getItem(int index) {
        return communityPostList.get(index);
    }

    public int getDuration() {
        return this.duration;
    }

    public List<CommunityListItem> getCommunityPostList() {
        return this.communityPostList;
    }
}
