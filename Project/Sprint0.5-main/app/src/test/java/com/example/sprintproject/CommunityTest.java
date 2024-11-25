package com.example.sprintproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.Community;
import com.example.sprintproject.model.CommunityListItem;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.Dining;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class CommunityTest {

    @Test
    public void addDestination_increasesDuration() {
        Community community = new Community();
        Destination destination = new Destination("Paris", "01/01/2023", "01/10/2023", 10);
        Accommodation accommodation = new Accommodation("1", "Hotel Paris", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Suite", 101);
        Dining dining = new Dining("1", "01/05/2023", "19:00", "Paris Restaurant", "www.parisrestaurant.com");
        String transportation = "Flight";

        community.addDestination(10, destination, accommodation, dining, transportation);

        assertEquals(10, community.getDuration());
    }

    @Test
    public void addDestination_addsToCommunityPostList() {
        Community community = new Community();
        Destination destination = new Destination("Paris", "01/01/2023", "01/10/2023", 10);
        Accommodation accommodation = new Accommodation("1", "Hotel Paris", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Suite", 101);
        Dining dining = new Dining("1", "01/05/2023", "19:00", "Paris Restaurant", "www.parisrestaurant.com");
        String transportation = "Flight";

        community.addDestination(10, destination, accommodation, dining, transportation);

        List<CommunityListItem> postList = community.getCommunityPostList();
        assertEquals(1, postList.size());
        assertNotNull(postList.get(0));
    }

    @Test
    public void getItem_returnsCorrectItem() {
        Community community = new Community();
        Destination destination = new Destination("Paris", "01/01/2023", "01/10/2023", 10);
        Accommodation accommodation = new Accommodation("1", "Hotel Paris", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Suite", 101);
        Dining dining = new Dining("1", "01/05/2023", "19:00", "Paris Restaurant", "www.parisrestaurant.com");
        String transportation = "Flight";

        community.addDestination(10, destination, accommodation, dining, transportation);

        CommunityListItem item = community.getItem(0);
        assertNotNull(item);
        assertEquals(destination, item.getDestination());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getItem_throwsExceptionForInvalidIndex() {
        Community community = new Community();
        community.getItem(0);
    }

    @Test
    public void getDuration_initiallyZero() {
        Community community = new Community();
        assertEquals(0, community.getDuration());
    }

    @Test
    public void getCommunityPostList_initiallyEmpty() {
        Community community = new Community();
        assertTrue(community.getCommunityPostList().isEmpty());
    }

    @Test
    public void multiplePosts() {
        Community community = new Community();

        Destination atlanta = new Destination("Atlanta", "05/01/2024", "05/08/2024", 8);
        Destination miami = new Destination("Miami", "05/09/2024", "05/16/2024", 8);

        Accommodation atl_hotel = new Accommodation("1", "Omni Atlanta", "Atlanta", LocalDateTime.now(), LocalDateTime.now().plusDays(8), "Suite", 1462);
        Accommodation mia_hotel = new Accommodation("2", "Hilton Miami", "Miami", LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(16), "Suite", 317);

        Dining atl_dining = new Dining("1", "05/05/2023", "19:00", "ATL Restaurant", "www.atlrestaurant.com");
        Dining mia_dining = new Dining("2", "05/14/2023", "19:00", "MIA Restaurant", "www.miarestaurant.com");

        String atl_transport = "Flight";
        String mia_transport = "Flight";

        community.addDestination(8, atlanta, atl_hotel, atl_dining, atl_transport);
        community.addDestination(8, miami, mia_hotel, mia_dining, mia_transport);

        List<CommunityListItem> postList = community.getCommunityPostList();
        assertEquals(2, postList.size());
        assertNotNull(postList.get(0));
        assertNotNull(postList.get(1));

        assertEquals("Atlanta", postList.get(0).getDestination().getLocation());
        assertEquals("Miami", postList.get(1).getDestination().getLocation());

        assertEquals(16, community.getDuration());
    }

    @Test
    public void repeatedDestination() {
        Community community = new Community();

        Destination atlanta = new Destination("Atlanta", "05/01/2024", "05/08/2024", 8);
        Accommodation atl_hotel = new Accommodation("1", "Omni Atlanta", "Atlanta", LocalDateTime.now(), LocalDateTime.now().plusDays(8), "Suite", 1462);
        Dining atl_dining = new Dining("1", "05/05/2023", "19:00", "ATL Restaurant", "www.atlrestaurant.com");
        String atl_transport = "Flight";

        community.addDestination(8, atlanta, atl_hotel, atl_dining, atl_transport);
        community.addDestination(8, atlanta, atl_hotel, atl_dining, atl_transport);

        List<CommunityListItem> postList = community.getCommunityPostList();
        assertEquals(2, postList.size());
        assertNotNull(postList.get(0));
        assertNotNull(postList.get(1));

        assertEquals(16, community.getDuration());
        assertEquals(postList.get(0).getAccommodation().getLocation(), postList.get(1).getAccommodation().getLocation());
    }

}