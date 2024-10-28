package com.example.sprintproject.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Destination;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogisticsActivity extends AppCompatActivity {

    private DatabaseReference userDatabaseReference;
    private DatabaseReference destinationsDatabaseReference;
    private FirebaseAuth mAuth;
    private PieChart pieChart;
    private Button btnVisualizeTrip;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);

        mAuth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        destinationsDatabaseReference = FirebaseDatabase.getInstance().getReference("destinations");

        btnVisualizeTrip = findViewById(R.id.btnVisualizeTrip);
        pieChart = findViewById(R.id.pieChart);

        btnVisualizeTrip.setOnClickListener(v -> {
            fetchAndDisplayChartData();
        });

        setupNavigationButtons();
        setupAdditionalButtons();
    }

    private void setupNavigationButtons() {
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        ImageButton destinationButton = findViewById(R.id.destination_button);
        ImageButton diningButton = findViewById(R.id.dining_button);
        ImageButton accomButton = findViewById(R.id.accommodation_button);
        ImageButton communityButton = findViewById(R.id.community_button);
        ImageButton transportButton = findViewById(R.id.transportation_button);

        logisticsButton.setSelected(true);
        logisticsButton.setOnClickListener(v -> {});

        destinationButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, DestinationActivity.class);
            startActivity(intent);
        });

        diningButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        accomButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, AccommodationActivity.class);
            startActivity(intent);
        });

        communityButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, CommunityActivity.class);
            startActivity(intent);
        });

        transportButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, Transportation.class);
            startActivity(intent);
        });
    }

    private void setupAdditionalButtons() {
        ImageButton contributorButton = findViewById(R.id.contributor_button);
        ImageButton notesButton = findViewById(R.id.notes_button);

        contributorButton.setOnClickListener(v -> {
            Toast.makeText(this, "Contributor button clicked", Toast.LENGTH_SHORT).show();
        });

        notesButton.setOnClickListener(v -> {
            Toast.makeText(this, "Notes button clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchAndDisplayChartData() {
        String userId = mAuth.getCurrentUser().getUid();

        userDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    String startVacationStr = userSnapshot.child("startVacation").getValue(String.class);
                    String endVacationStr = userSnapshot.child("endVacation").getValue(String.class);

                    if (startVacationStr == null || endVacationStr == null) {
                        Toast.makeText(LogisticsActivity.this, "Vacation dates not set", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Date startVacationDate, endVacationDate;
                    try {
                        startVacationDate = dateFormat.parse(startVacationStr);
                        endVacationDate = dateFormat.parse(endVacationStr);
                    } catch (ParseException e) {
                        Toast.makeText(LogisticsActivity.this, "Invalid vacation date format", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (startVacationDate == null || endVacationDate == null || startVacationDate.after(endVacationDate)) {
                        Toast.makeText(LogisticsActivity.this, "Invalid vacation dates", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int vacationDuration = calculateDaysBetween(startVacationDate, endVacationDate);

                    DataSnapshot destinationsSnapshot = userSnapshot.child("destinations");
                    if (destinationsSnapshot.exists()) {
                        List<String> destinationIds = new ArrayList<>();
                        for (DataSnapshot destinationEntry : destinationsSnapshot.getChildren()) {
                            String destinationId = destinationEntry.getValue(String.class);
                            if (destinationId != null) {
                                destinationIds.add(destinationId);
                            }
                        }

                        if (!destinationIds.isEmpty()) {
                            fetchDestinationsAndCalculateTotalDuration(destinationIds, vacationDuration, startVacationDate, endVacationDate);
                        } else {
                            displayPieChart(vacationDuration, 0);
                        }
                    } else {
                        displayPieChart(vacationDuration, 0);
                    }
                } else {
                    Toast.makeText(LogisticsActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LogisticsActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDestinationsAndCalculateTotalDuration(List<String> destinationIds, int vacationDuration, Date startVacationDate, Date endVacationDate) {
        final int[] destinationsFetched = {0};
        final int totalDestinations = destinationIds.size();
        final int[] totalPlannedDays = {0};
        final int[] overlappingDays = {0};

        if (totalDestinations == 0) {
            displayPieChart(vacationDuration, 0);
            return;
        }

        for (String destinationId : destinationIds) {
            destinationsDatabaseReference.child(destinationId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot destinationSnapshot) {
                    destinationsFetched[0]++;
                    if (destinationSnapshot.exists()) {
                        Destination destination = destinationSnapshot.getValue(Destination.class);
                        if (destination != null) {
                            int tripDuration = destination.getDuration();
                            totalPlannedDays[0] += tripDuration;

                            try {
                                Date tripStartDate = dateFormat.parse(destination.getStartDate());
                                Date tripEndDate = dateFormat.parse(destination.getEndDate());

                                if (tripStartDate != null && tripEndDate != null) {
                                    int overlap = calculateOverlappingDays(tripStartDate, tripEndDate, startVacationDate, endVacationDate);
                                    overlappingDays[0] += overlap;
                                }
                            } catch (Exception e) {
                                // error
                            }
                        }
                    }
                    if (destinationsFetched[0] == totalDestinations) {
                        int adjustedPlannedDays = totalPlannedDays[0] - overlappingDays[0];
                        displayPieChart(vacationDuration + overlappingDays[0], adjustedPlannedDays);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    destinationsFetched[0]++;
                    if (destinationsFetched[0] == totalDestinations) {
                        int adjustedPlannedDays = totalPlannedDays[0] - overlappingDays[0];
                        displayPieChart(vacationDuration + overlappingDays[0], adjustedPlannedDays);
                    }
                }
            });
        }
    }

    private int calculateDaysBetween(Date startDate, Date endDate) {
        int diffInMillis = (int) (endDate.getTime() - startDate.getTime());
        return (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
    }

    private int calculateOverlappingDays(Date tripStart, Date tripEnd, Date vacationStart, Date vacationEnd) {
        Date maxStart = tripStart.after(vacationStart) ? tripStart : vacationStart;
        Date minEnd = tripEnd.before(vacationEnd) ? tripEnd : vacationEnd;
        if (maxStart.after(minEnd)) {
            return 0;
        }
        return calculateDaysBetween(maxStart, minEnd);
    }

    private void displayPieChart(int vacationDays, int plannedTripDays) {
        runOnUiThread(() -> {
            int vacDays = vacationDays;
            int planTripDays = plannedTripDays;

            if (vacDays < 0) {
                vacDays = 0;
            }
            if (planTripDays < 0) {
                planTripDays = 0;
            }

            int totalDays = vacDays + planTripDays;

            if (totalDays == 0) {
                Toast.makeText(LogisticsActivity.this, "No data to display in chart", Toast.LENGTH_SHORT).show();
                pieChart.setVisibility(android.view.View.GONE);
                return;
            }

            List<PieEntry> entries = new ArrayList<>();
            if (vacDays > 0) {
                entries.add(new PieEntry(vacDays, "Vacation Days"));
            }
            if (planTripDays > 0) {
                entries.add(new PieEntry(planTripDays, "Planned Trip Days"));
            }

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(new int[]{Color.GREEN, Color.BLUE});
            PieData data = new PieData(dataSet);
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);

            pieChart.setData(data);
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.setDrawHoleEnabled(false);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setEntryLabelTextSize(12f);
            pieChart.animateY(1000);

            pieChart.invalidate();
            pieChart.setVisibility(android.view.View.VISIBLE);
        });
    }
}
