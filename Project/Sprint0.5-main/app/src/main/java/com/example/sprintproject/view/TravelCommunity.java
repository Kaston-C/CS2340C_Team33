package com.example.sprintproject.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.sprintproject.R;  // Adjust this import based on your package structure

public class TravelCommunity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_community);  // Ensure this layout file contains the BottomNavigationView

        // Find the BottomNavigationView by its ID
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.community_button);  // Set default selection

        // Set up the listener for navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Navigate based on the selected item
            if (itemId == R.id.community_button) {
                // Already on this screen, so do nothing
                return true;
            } else if (itemId == R.id.destination_button) {
                startActivity(new Intent(TravelCommunity.this, DestinationActivity.class));
                return true;
            } else if (itemId == R.id.dining_button) {
                startActivity(new Intent(TravelCommunity.this, DiningActivity.class));
                return true;
            } else if(itemId == R.id.accommodation_button){
                startActivity(new Intent(TravelCommunity.this, AccomodationActivity.class));
                return true;
            } else if(itemId == R.id.logistics_button) {
                startActivity(new Intent(TravelCommunity.this, LogisticsActivity.class));
                return true;
            }
            return false;  // Return false if no match
        });
    }
}
