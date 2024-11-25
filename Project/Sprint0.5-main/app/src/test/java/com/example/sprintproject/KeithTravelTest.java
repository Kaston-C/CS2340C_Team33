package com.example.sprintproject;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.Community;
import com.example.sprintproject.model.CommunityListItem;
import com.example.sprintproject.model.Destination;

import static org.junit.Assert.assertEquals;

import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.Dining;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class KeithTravelTest {
    //Test if the data from the form gets into the database and is accedssible
    @Test
    public void testDurationInitialization() {
        Community community = new Community();
        int duration = community.getDuration();
        assertEquals(duration, 0);
    }

    @Test
    public void testCommunityPostList() {
        Community community = new Community();
        int duration = community.getDuration();
        assertEquals(duration, 0);

        List<CommunityListItem> communityPostList = community.getCommunityPostList();
        assertEquals(communityPostList, new ArrayList<CommunityListItem>() {});

        community.addDestination(10, new Destination(), new Accommodation(), new Dining(), "Car");
        assertEquals(duration + 10, community.getDuration());

        CommunityListItem newItem = community.getItem(0);
        assertEquals(communityPostList.get(0), newItem);
    }
}
