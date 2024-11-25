package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.FilterStrategy;
import com.example.sprintproject.model.SortByTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AccommodationActivity extends AppCompatActivity {

    private DatabaseReference tripDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference accommodationDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerViewAccommodation;
    private AccommodationAdapter accommodationAdapter;
    private List<Accommodation> accommodationList;
    private FilterStrategy filterStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);
        setUpButtons();

        accommodationDatabaseReference = FirebaseDatabase.getInstance().getReference("accommodations");
        tripDatabaseReference = FirebaseDatabase.getInstance().getReference("trips");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in correctly.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = currentUser.getUid();

        Button addAccommodationButton = findViewById(R.id.add_accommodation_button);
        LinearLayout formContainer = findViewById(R.id.form_container);
        Button submitButton = findViewById(R.id.submit_button);
        EditText checkInDate = findViewById(R.id.check_in_date);
        EditText checkOutDate = findViewById(R.id.check_out_date);
        EditText location = findViewById(R.id.location);
        EditText name = findViewById(R.id.name);
        EditText numberOfRooms = findViewById(R.id.number_of_rooms);
        EditText roomType = findViewById(R.id.room_type);
        recyclerViewAccommodation = findViewById(R.id.accommodation_recycler_view);

        recyclerViewAccommodation.setLayoutManager(new LinearLayoutManager(this));
        accommodationAdapter = new AccommodationAdapter(accommodationList);
        recyclerViewAccommodation.setAdapter(accommodationAdapter);

        accommodationList = new ArrayList<>();
        fetchAccommodations();

        formContainer.setVisibility(View.GONE);

        addAccommodationButton.setOnClickListener(v -> {
            addAccommodationButton.setVisibility(View.GONE);
            formContainer.setVisibility(View.VISIBLE);
        });
        submitButton.setOnClickListener(v -> {
            String checkInDateStr = checkInDate.getText().toString();
            String checkOutDateStr = checkOutDate.getText().toString();
            String locationStr = location.getText().toString();
            String nameStr = name.getText().toString();
            String numberOfRoomsStr = numberOfRooms.getText().toString();
            String roomTypeStr = roomType.getText().toString();
            if (!Accommodation.isValidDate(checkInDateStr)
                    || !Accommodation.isValidDate(checkOutDateStr)) {
                Toast.makeText(this,
                        "Invalid date format. Please use MM/DD/YYYY.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Accommodation.isCheckOutDateValid(checkInDateStr, checkOutDateStr)) {
                Toast.makeText(this, "Check-out date must be on or after the check-in date.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (locationStr.isEmpty() || nameStr.isEmpty() || numberOfRoomsStr.isEmpty()
                    || roomTypeStr.isEmpty()) {
                Toast.makeText(this,
                        "All fields must be filled out.", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int numberOfRoomsInt = Integer.parseInt(numberOfRoomsStr);

                String id = UUID.randomUUID().toString();
                Accommodation accommodation = new Accommodation();
                accommodation.setCheckInDate(checkInDateStr);
                accommodation.setCheckOutDate(checkOutDateStr);
                accommodation.setLocation(locationStr);
                accommodation.setName(nameStr);
                accommodation.setNumberOfRooms(numberOfRoomsInt);
                accommodation.setRoomType(roomTypeStr);
                userDatabaseReference.child(userId).child("trip")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String tripId = dataSnapshot.getValue(String.class);
                                     accommodationDatabaseReference.child(id)
                                                .setValue(accommodation)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        tripDatabaseReference.child(tripId)
                                                                .child("accommodations")
                                                                .child(accommodation.getName())
                                                                .setValue(id)
                                                                .addOnCompleteListener(addKeyTask -> {
                                                                });
                                                    }
                                                });
                                    }
                                }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                Toast.makeText(this,
                        "Accommodation added successfully.", Toast.LENGTH_SHORT).show();
                formContainer.setVisibility(View.GONE);
                addAccommodationButton.setVisibility(View.VISIBLE);

                checkInDate.setText("");
                checkOutDate.setText("");
                location.setText("");
                name.setText("");
                numberOfRooms.setText("");
                roomType.setText("");

            } catch (NumberFormatException e) {
                Toast.makeText(this,
                        "Number of Rooms must be a valid number.", Toast.LENGTH_SHORT).show();
            }
        });

        Button sortCheckIn = findViewById(R.id.sort_by_checkIn);
        sortCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterStrategy = new SortByTime();
                List<Accommodation> filteredList = filterStrategy
                        .filter(accommodationList, "checkIn");
                accommodationAdapter.updateList(filteredList);
            }
        });

        Button sortCheckOut = findViewById(R.id.sort_by_checkOut);
        sortCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterStrategy = new SortByTime();
                List<Accommodation> filteredList = filterStrategy
                        .filter(accommodationList, "checkOut");
                accommodationAdapter.updateList(filteredList);
            }
        });
    }

    private void setUpButtons() {
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });
        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationActivity.this, DestinationActivity.class);
            startActivity(intent);
        });
        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        accomButton.setSelected(true);
        accomButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationActivity.this, AccommodationActivity.class);
            startActivity(intent);
        });

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationActivity.this, CommunityActivity.class);
            startActivity(intent);
        });

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationActivity.this, Transportation.class);
            startActivity(intent);
        });
    }

    private void fetchAccommodations() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        userDatabaseReference.child(userId).child("trip")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String tripId = dataSnapshot.getValue(String.class);

                            tripDatabaseReference.child(tripId).child("accommodations")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            accommodationList.clear();

                                            for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                                                String accommodationKey = keySnapshot.getValue(String.class);

                                                accommodationDatabaseReference.child(accommodationKey)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot accommodationSnapshot) {
                                                                Accommodation accommodation = accommodationSnapshot.getValue(Accommodation.class);
                                                                Log.d("DEBUG", "DataSnapshot exists: " + dataSnapshot.exists());
                                                                Log.d("DEBUG", "DataSnapshot value: " + dataSnapshot.getValue());
                                                                if (accommodation != null) {
                                                                    accommodationList.add(accommodation);

                                                                    sortAccommodationsByCheckInDate(accommodationList);
                                                                    accommodationAdapter.notifyDataSetChanged();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError error) {
                                                                Toast.makeText(AccommodationActivity.this,
                                                                        "Failed to load accommodation details.",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(AccommodationActivity.this,
                                                    "Failed to load accommodation keys.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(AccommodationActivity.this, "Failed to load trip data.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void sortAccommodationsByCheckInDate(List<Accommodation> accommodations) {
        Collections.sort(accommodations, new Comparator<Accommodation>() {
            @Override
            public int compare(Accommodation o1, Accommodation o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date date1 = parseDate(o1.getCheckInDate(), sdf);
                Date date2 = parseDate(o2.getCheckInDate(), sdf);

                if (date1 == null || date2 == null) {
                    return 0;
                }

                return date1.compareTo(date2);
            }
        });
    }

    private Date parseDate(String dateStr, SimpleDateFormat sdf) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
