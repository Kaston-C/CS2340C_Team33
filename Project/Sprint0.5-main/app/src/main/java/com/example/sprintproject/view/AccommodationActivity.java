package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class AccommodationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set current view to accommodation activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);

        //set menu buttons to switch current screen
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, LogisticsActivity.class)));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, DestinationActivity.class)));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, DiningActivity.class)));

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        //accomButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, AccommodationActivity.class)));
        accomButton.setSelected(true);

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, CommunityActivity.class)));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, Transportation.class)));


    }
}
