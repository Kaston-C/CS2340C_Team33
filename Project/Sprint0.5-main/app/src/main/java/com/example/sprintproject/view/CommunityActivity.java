package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;

public class CommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(
                v -> startActivity(new Intent(CommunityActivity.this, LogisticsActivity.class)));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(
                v -> startActivity(new Intent(CommunityActivity.this, DestinationActivity.class)));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(
                v -> startActivity(new Intent(CommunityActivity.this, DiningActivity.class)));

        ImageButton accommodationButton = findViewById(R.id.accommodation_button);
        accommodationButton.setOnClickListener(
                v -> startActivity(
                        new Intent(CommunityActivity.this, AccommodationActivity.class)));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setSelected(true);

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(
                v -> startActivity(new Intent(CommunityActivity.this, Transportation.class)));

        Button addPostButton = findViewById(R.id.add_post_button);
        addPostButton.setOnClickListener(
                v -> {
                Intent intent = new Intent(CommunityActivity.this, PostActivity.class);
                startActivity(intent);
                });
    }
}
