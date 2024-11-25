package com.example.sprintproject.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.FilterStrategy;
import com.example.sprintproject.model.SortByTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiningActivity extends AppCompatActivity {

    private DatabaseReference diningDatabaseReference;
    private DatabaseReference tripDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private DiningAdapter diningAdapter;
    private List<Dining> diningList;
    private FilterStrategy filterStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining);

        diningDatabaseReference = FirebaseDatabase.getInstance().getReference("dining");
        tripDatabaseReference = FirebaseDatabase.getInstance().getReference("trips");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rvDiningReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        diningList = new ArrayList<>();
        diningAdapter = new DiningAdapter(diningList);
        recyclerView.setAdapter(diningAdapter);

        loadDiningReservations();

        Button btnAddReservation = findViewById(R.id.btnAddReservation);
        btnAddReservation.setOnClickListener(v -> showAddReservationDialog());

        Button sortButton = findViewById(R.id.btnSort);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterStrategy = new SortByTime();
                List<Dining> filteredList = filterStrategy.filter(diningList, "time");
                diningAdapter.updateList(filteredList);
            }
        });

        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> startActivity(
                new Intent(DiningActivity.this, LogisticsActivity.class)
        ));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(v -> startActivity(
                new Intent(DiningActivity.this, DestinationActivity.class)
        ));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setSelected(true);
        diningButton.setOnClickListener(v -> startActivity(
                new Intent(DiningActivity.this, DiningActivity.class)
        ));

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        accomButton.setOnClickListener(v -> startActivity(
                new Intent(DiningActivity.this, AccommodationActivity.class)
        ));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> startActivity(
                new Intent(DiningActivity.this, CommunityActivity.class)
        ));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> startActivity(
                new Intent(DiningActivity.this, Transportation.class)
        ));
    }

    private void showAddReservationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_reservation, null);
        builder.setView(dialogView);

        EditText etDate = dialogView.findViewById(R.id.etDate);
        EditText etTime = dialogView.findViewById(R.id.etTime);
        EditText etLocation = dialogView.findViewById(R.id.etLocation);
        EditText etWebsite = dialogView.findViewById(R.id.etWebsite);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);

        AlertDialog dialog = builder.create();

        btnAdd.setOnClickListener(v -> {
            String date = etDate.getText().toString();
            String time = etTime.getText().toString();
            String location = etLocation.getText().toString();
            String website = etWebsite.getText().toString();

            if (!date.isEmpty() && !time.isEmpty() && !location.isEmpty() && !website.isEmpty()) {
                if (Dining.isValidDateTimeFormat(date, time)) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        String id = UUID.randomUUID().toString();
                        Dining dining = new Dining(date, time, location, website);
                        diningDatabaseReference.child(id).setValue(dining)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        userDatabaseReference.child(currentUser.getUid())
                                                .child("trip")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            String tripId = dataSnapshot.getValue(String.class);
                                                            if (tripId != null) {
                                                                tripDatabaseReference.child(tripId)
                                                                        .child("dining")
                                                                        .child(dining.getLocation())
                                                                        .setValue(id)
                                                                        .addOnCompleteListener(task -> {
                                                                        });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        Toast.makeText(getApplication(),
                                                                "Failed to save destination",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                        Toast.makeText(this, "Reservation added!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "User not authenticated. Please log in.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid date or time format."
                                    + "Please use MM/dd/yyyy for date and HH:mm for time.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill in all fields",
                        Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    private void loadDiningReservations() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        userDatabaseReference.child(userId).child("trip")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String tripId = dataSnapshot.getValue(String.class);
                            tripDatabaseReference.child(tripId).child("dining")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            diningList.clear();
                                            DateTimeFormatter formatter = DateTimeFormatter
                                                    .ofPattern("MM/dd/yyyy HH:mm");

                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                String diningKey = dataSnapshot.getValue(String.class);
                                                if (diningKey != null) {
                                                    diningDatabaseReference.child(diningKey)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot diningSnapshot) {
                                                                    Dining dining = diningSnapshot.getValue(Dining.class);
                                                                    if (dining != null) {
                                                                        diningList.add(dining);
                                                                        diningAdapter.notifyDataSetChanged();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError error) {
                                                                    Toast.makeText(getApplication(),
                                                                            "Failed to fetch dining details",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplication(),
                                                    "Failed to load dining keys",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(DiningActivity.this, "Failed to load trip data.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
