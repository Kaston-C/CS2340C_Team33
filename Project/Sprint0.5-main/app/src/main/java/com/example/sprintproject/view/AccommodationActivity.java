package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
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

public class AccommodationActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerViewAccommodation;
    private AccommodationAdapter accommodationAdapter;
    private List<Accommodation> accommodationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);

        // Initialize Firebase Database reference
        firebaseAuth = FirebaseAuth.getInstance();


        //Specific to USER
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User did not log in correctly", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("accommodation").child(userId);

        recyclerViewAccommodation = findViewById(R.id.accommodation_recycler_view);
        recyclerViewAccommodation.setLayoutManager(new LinearLayoutManager(this));
        accommodationList = new ArrayList<>();
        accommodationAdapter = new AccommodationAdapter(accommodationList);
        recyclerViewAccommodation.setAdapter(accommodationAdapter);


        //Get accomodations from firebase
        fetchAccommodations();


        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, LogisticsActivity.class)));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, DestinationActivity.class)));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, DiningActivity.class)));

        ImageButton accommodationButton = findViewById(R.id.accommodation_button);
        accommodationButton.setSelected(true);
        accommodationButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, AccommodationActivity.class)));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, CommunityActivity.class)));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> startActivity(new Intent(AccommodationActivity.this, Transportation.class)));

        // Form fields
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

                // Save to firebase
                databaseReference.child(id).setValue(accommodation);
                Toast.makeText(this, "Accommodation saved", Toast.LENGTH_SHORT).show();

                // After saving, update the list and refresh the view
                fetchAccommodations();
            } else {
                Toast.makeText(this, "Could not save accommodation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method to get the accomodations from firebase
    private void fetchAccommodations() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accommodationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Accommodation accommodation = snapshot.getValue(Accommodation.class);
                    if (accommodation != null) {
                        accommodationList.add(accommodation);
                    }
                }
                sortAccommodationsByCheckInDate(accommodationList);  //SORTING
                accommodationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AccommodationActivity.this, "Failed to include accommodations.", Toast.LENGTH_SHORT).show();
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
