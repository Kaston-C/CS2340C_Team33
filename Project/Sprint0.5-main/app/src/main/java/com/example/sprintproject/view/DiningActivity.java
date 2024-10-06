package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class DiningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set current view to dining activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining);


        //set menu buttons to switch current screen
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> startActivity(new Intent(DiningActivity.this, LogisticsActivity.class)));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(v -> startActivity(new Intent(DiningActivity.this, DestinationActivity.class)));

        ImageButton diningButton = findViewById(R.id.dining_button);
        //diningButton.setOnClickListener(v -> startActivity(new Intent(DiningActivity.this, DiningActivity.class)));
        diningButton.setSelected(true);

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        accomButton.setOnClickListener(v -> startActivity(new Intent(DiningActivity.this, AccommodationActivity.class)));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> startActivity(new Intent(DiningActivity.this, CommunityActivity.class)));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> startActivity(new Intent(DiningActivity.this, Transportation.class)));

    }

}
