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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Dining;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DiningActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private DiningAdapter diningAdapter;
    private List<Dining> diningList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining);

        databaseReference = FirebaseDatabase.getInstance().getReference("dining");
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rvDiningReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        diningList = new ArrayList<>();
        diningAdapter = new DiningAdapter(diningList);
        recyclerView.setAdapter(diningAdapter);

        loadDiningReservations();

        Button btnAddReservation = findViewById(R.id.btnAddReservation);
        btnAddReservation.setOnClickListener(v -> showAddReservationDialog());

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
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    String id = databaseReference.push().getKey();
                    Dining dining = new Dining(id, date, time, location, website);
                    databaseReference.child(id).setValue(dining);
                    Toast.makeText(this, "Reservation added!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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

    private void loadDiningReservations() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diningList.clear();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Dining dining = dataSnapshot.getValue(Dining.class);
                    if (dining != null) {
                        diningList.add(dining);
                    }
                }

                Collections.sort(diningList, new Comparator<Dining>() {
                    @Override
                    public int compare(Dining d1, Dining d2) {
                        LocalDateTime dateTime1 = LocalDateTime.parse(d1.getDate()
                                + " " + d1.getTime(), formatter);
                        LocalDateTime dateTime2 = LocalDateTime.parse(d2.getDate()
                                + " " + d2.getTime(), formatter);
                        return dateTime1.compareTo(dateTime2);
                    }
                });
                diningAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DiningActivity.this, "Failed to load data.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
