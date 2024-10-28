package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private Integer allottedDays;
    private Integer plannedDays = 0;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setVisibility(View.GONE);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        setupNavButtons();
        getUserData();

        Button btnVisualizeTrip = findViewById(R.id.btnVisualizeTrip);
        btnVisualizeTrip.setOnClickListener(v -> {
            int days = (allottedDays != null ? allottedDays : 0);
            showPieChart(days, plannedDays);
        });
    }

    private void setupNavButtons() {
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setSelected(true);
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

    private void getUserData() {
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(userId);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    allottedDays = dataSnapshot.child("totalDays").getValue(Integer.class);
                    getPlannedDays();
                } else {
                    allottedDays = 0;
                    plannedDays = 0;
                    showPieChart(allottedDays, plannedDays);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LogisticsActivity.this, "Failed" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPlannedDays() {
        DatabaseReference destinationsRef = FirebaseDatabase.getInstance().getReference("destinations").child(userId);

        destinationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                plannedDays = 0; // Reset plannedDays

                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    Integer duration = x.child("duration").getValue(Integer.class);
                    if (duration != null) {
                        plannedDays += duration;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LogisticsActivity.this, "Failed " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPieChart(int allottedDays, int plannedDays) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(plannedDays, "Planned Days"));
        entries.add(new PieEntry(Math.max(0, allottedDays - plannedDays), "Allotted Days"));

        PieDataSet dataSet = new PieDataSet(entries, "Trip Days");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.setVisibility(View.VISIBLE);
    }
}
