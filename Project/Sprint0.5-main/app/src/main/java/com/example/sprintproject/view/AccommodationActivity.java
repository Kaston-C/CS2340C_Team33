package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccommodationActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set current view to accommodation activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("accommodation");

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        //set menu buttons to switch current screen
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> startActivity(
                new Intent(AccommodationActivity.this, LogisticsActivity.class)
        ));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(v -> startActivity(
                new Intent(AccommodationActivity.this, DestinationActivity.class)
        ));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> startActivity(
                new Intent(AccommodationActivity.this, DiningActivity.class)
        ));

        ImageButton accommodationButton = findViewById(R.id.accommodation_button);
        accommodationButton.setSelected(true);
        accommodationButton.setOnClickListener(v -> startActivity(
                new Intent(AccommodationActivity.this, AccommodationActivity.class)
        ));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> startActivity(
                new Intent(AccommodationActivity.this, CommunityActivity.class)
        ));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> startActivity(
                new Intent(AccommodationActivity.this, Transportation.class)
        ));

        EditText checkInDate = findViewById(R.id.check_in_date);
        EditText checkOutDate = findViewById(R.id.check_out_date);
        EditText location = findViewById(R.id.location);
        EditText name = findViewById(R.id.name);
        EditText numberOfRooms = findViewById(R.id.number_of_rooms);
        EditText roomType = findViewById(R.id.room_type);
        Button submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(v -> {
            String checkInDateStr = checkInDate.getText().toString();
            String checkOutDateStr = checkOutDate.getText().toString();

            if (!Accommodation.isValidDate(checkInDateStr) || !Accommodation.isValidDate(checkOutDateStr)) {
                Toast.makeText(this, "Invalid date format. Please use MM/DD/YYYY.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Accommodation.isCheckOutDateValid(checkInDateStr, checkOutDateStr)) {
                Toast.makeText(this, "Check-out date must be on or after the check-in date.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                String id = databaseReference.push().getKey();
                Accommodation accommodation = new Accommodation();
                accommodation.setId(id);
                accommodation.setCheckInDate(checkInDateStr);
                accommodation.setCheckOutDate(checkOutDateStr);
                accommodation.setLocation(location.getText().toString());
                accommodation.setName(name.getText().toString());
                accommodation.setNumberOfRooms(Integer.parseInt(numberOfRooms.getText().toString()));
                accommodation.setRoomType(roomType.getText().toString());

                databaseReference.child(id).setValue(accommodation);
                Toast.makeText(this, "Accommodation saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Could not save accommodation", Toast.LENGTH_SHORT).show();
            }
        });
    }
}