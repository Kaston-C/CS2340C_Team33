package com.example.sprintproject.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class LogisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set current view to logistics activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);

        //set menu buttons to switch current screen
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setBackground(Drawable.createFromPath("@drawable/active_button.xml") );
        logisticsButton.setOnClickListener(v -> startActivity(new Intent(LogisticsActivity.this, LogisticsActivity.class)));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(v -> startActivity(new Intent(LogisticsActivity.this, DestinationActivity.class)));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> startActivity(new Intent(LogisticsActivity.this, DiningActivity.class)));

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        accomButton.setOnClickListener(v -> startActivity(new Intent(LogisticsActivity.this, AccommodationActivity.class)));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> startActivity(new Intent(LogisticsActivity.this, CommunityActivity.class)));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> startActivity(new Intent(LogisticsActivity.this, Transportation.class)));



    }
}
