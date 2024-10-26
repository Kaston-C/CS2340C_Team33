package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodel.DestinationViewModel;
import com.example.sprintproject.viewmodel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class DestinationActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set current view to destination activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        //set menu buttons to switch current screen
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> startActivity(
                new Intent(DestinationActivity.this, LogisticsActivity.class)
        ));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setSelected(true);
        destinationButton.setOnClickListener(v -> startActivity(
                new Intent(DestinationActivity.this, DestinationActivity.class)
        ));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> startActivity(
                new Intent(DestinationActivity.this, DiningActivity.class)
        ));

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        accomButton.setOnClickListener(v -> startActivity(
                new Intent(DestinationActivity.this, AccommodationActivity.class)
        ));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> startActivity(
                new Intent(DestinationActivity.this, CommunityActivity.class)
        ));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> startActivity(
                new Intent(DestinationActivity.this, Transportation.class)
        ));

        databaseReference = DestinationViewModel.firebaseConnect();
        mAuth = LoginViewModel.firebaseAuthorization();

        //sets up start, end, and destination fields
        EditText startInput = findViewById(R.id.start_input);
        EditText endInput = findViewById(R.id.end_input);
        EditText travelInput = findViewById(R.id.destination_input);

        //sets up submit and cancel log button
        Button submitDestination = findViewById(R.id.submit_log_button);
        Button CancelDestination = findViewById(R.id.cancel_log_button);

        submitDestination.setOnClickListener(view -> {
            String start = DestinationViewModel.getInputStart(startInput);
            String end = DestinationViewModel.getInputEnd(endInput);
            String destination = DestinationViewModel.getInputDestination(travelInput);

            databaseReference.child("destinations").child(destination).setValue(start + " - " + end);
        });
    }
}

    