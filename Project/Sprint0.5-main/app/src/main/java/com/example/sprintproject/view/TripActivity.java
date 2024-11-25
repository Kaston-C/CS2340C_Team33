package com.example.sprintproject.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.sprintproject.model.FilterStrategy;
import com.example.sprintproject.model.SortByTime;
import com.example.sprintproject.model.Trip;
import com.example.sprintproject.model.User;
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

public class TripActivity extends AppCompatActivity {

    private DatabaseReference tripDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private List<Trip> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        tripDatabaseReference = FirebaseDatabase.getInstance().getReference("trips");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rvTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripList = new ArrayList<>();
        loadTripReservations();

        Button btnAddTrip = findViewById(R.id.btnAddTrip);
        btnAddTrip.setOnClickListener(v -> showAddTripDialog());

        tripAdapter = new TripAdapter(this, tripList);
        recyclerView.setAdapter(tripAdapter);
    }

    private void showAddTripDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_trip, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);

        AlertDialog dialog = builder.create();

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString();

            if (!name.isEmpty()) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        String id = UUID.randomUUID().toString();
                        Trip trip = new Trip(name);
                        tripDatabaseReference.child(id).setValue(trip)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        userDatabaseReference.child(currentUser.getUid())
                                                .child("trips").child(name).setValue(id);
                                    }
                                });
                        Toast.makeText(this, "Trip added!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadTripReservations();
                    } else {
                        Toast.makeText(this, "User not authenticated. Please log in.",
                                Toast.LENGTH_SHORT).show();
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields",
                        Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    private void loadTripReservations() {
        userDatabaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Trip> tempList = new ArrayList<>();
                int totalTrips = (int) dataSnapshot.getChildrenCount();
                if (totalTrips == 0) {
                    tripList.clear();
                }
                int[] loadedCount = {0};
                for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                    String tripID = tripSnapshot.getValue(String.class);
                    fetchTripDetails(tripID, tempList, loadedCount, totalTrips);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void fetchTripDetails(String tripID, ArrayList<Trip> tempList, int[] loadedCount, int totalTrips) {
            DatabaseReference tripRef = tripDatabaseReference.child(tripID);

            tripRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Trip trip = dataSnapshot.getValue(Trip.class);
                    if (trip != null) {
                        tempList.add(trip);
                    }
                    loadedCount[0]++;

                    if (loadedCount[0] == totalTrips) {
                        tripList.clear();
                        tripList.addAll(tempList);

                        tripAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }
}
