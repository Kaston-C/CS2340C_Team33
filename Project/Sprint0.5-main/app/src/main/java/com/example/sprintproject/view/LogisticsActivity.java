package com.example.sprintproject.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.ActivityDestinationBinding;
import com.example.sprintproject.databinding.ActivityLogisticsBinding;
import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.User;
import com.example.sprintproject.viewmodel.LogisticsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogisticsActivity extends AppCompatActivity {

    private DatabaseReference userDatabaseReference;
    private DatabaseReference tripDatabaseReference;
    private DatabaseReference destinationsDatabaseReference;
    private FirebaseAuth mAuth;
    private PieChart pieChart;
    private Button btnVisualizeTrip;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private LogisticsViewModel viewModel;
    private RecyclerView logisticsRecyclerView;
    private LogisticsAdapter adapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);

        ActivityLogisticsBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_logistics);
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        mAuth = DatabaseSingleton.getDatabase().getFirebaseAuthorization();
        userDatabaseReference = DatabaseSingleton.getDatabase().userDb();
        tripDatabaseReference = DatabaseSingleton.getDatabase().tripDb();
        destinationsDatabaseReference = DatabaseSingleton.getDatabase().destinationsDb();

        btnVisualizeTrip = findViewById(R.id.btnVisualizeTrip);
        pieChart = findViewById(R.id.pieChart);

        btnVisualizeTrip.setOnClickListener(v -> {
            fetchAndDisplayChartData();
        });

        setupNavigationButtons();
        setupAdditionalButtons();
        setupLogisticsRecyclerView(binding);
    }

    private void setupNavigationButtons() {
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        ImageButton destinationButton = findViewById(R.id.destination_button);
        ImageButton diningButton = findViewById(R.id.dining_button);
        ImageButton accomButton = findViewById(R.id.accommodation_button);
        ImageButton communityButton = findViewById(R.id.community_button);
        ImageButton transportButton = findViewById(R.id.transportation_button);

        logisticsButton.setSelected(true);
        logisticsButton.setOnClickListener(v -> { });

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
            final EditText input = new EditText(this);
            input.setHint("Enter contributor's username");

            new AlertDialog.Builder(this)
                    .setTitle("Add a contributor")
                    .setView(input)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String friendName = input.getText().toString().trim();
                        if (!friendName.isEmpty()) {
                            if (!friendName.contains("@")) {
                                friendName = friendName.concat("@wandersync.com");
                            }
                            viewModel.onSubmitContributor(friendName);
                        } else {
                            Toast.makeText(getApplicationContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        notesButton.setOnClickListener(v -> {
            final EditText input = new EditText(this);
            input.setHint("Enter note");

            new AlertDialog.Builder(this)
                    .setTitle("Add a note")
                    .setView(input)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String note = input.getText().toString().trim();
                        if (!note.isEmpty()) {
                            viewModel.onSubmitNote(note);
                        } else {
                            Toast.makeText(getApplicationContext(), "Note cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();        });
    }

    private void setupLogisticsRecyclerView(ActivityLogisticsBinding binding) {
        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(viewModel.getContributorList());
        combinedList.addAll(viewModel.getNoteList());

        adapter = new LogisticsAdapter(combinedList, new LogisticsAdapter.OnLogisticsClickListener() {
            @Override
            public void onLogisticsClick(Object object) {
                if (object instanceof User) {
                    showUserDetails((User) object);
                } else {
                    showNoteDetails((String) object);
                }
            }
        });

        binding.logisticsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.logisticsRecyclerView.setAdapter(adapter);

        viewModel.getContributorList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<User>>() {
            @Override
            public void onChanged(ObservableList<User> sender) {
                refreshCombinedList();
            }

            @Override
            public void onItemRangeChanged(ObservableList<User> sender, int positionStart, int itemCount) {
                refreshCombinedList();
            }

            @Override
            public void onItemRangeInserted(ObservableList<User> sender, int positionStart, int itemCount) {
                refreshCombinedList();
            }

            @Override
            public void onItemRangeMoved(ObservableList<User> sender, int fromPosition, int toPosition, int itemCount) {
                refreshCombinedList();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<User> sender, int positionStart, int itemCount) {
                refreshCombinedList();
            }
        });

        viewModel.getNoteList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<String>>() {
            @Override
            public void onChanged(ObservableList<String> sender) {
                refreshCombinedList();
            }

            @Override
            public void onItemRangeChanged(ObservableList<String> sender, int positionStart, int itemCount) {
                refreshCombinedList();
            }

            @Override
            public void onItemRangeInserted(ObservableList<String> sender, int positionStart, int itemCount) {
                refreshCombinedList();
            }

            @Override
            public void onItemRangeMoved(ObservableList<String> sender, int fromPosition, int toPosition, int itemCount) {
                refreshCombinedList();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<String> sender, int positionStart, int itemCount) {
                refreshCombinedList();
            }
        });
    }

    private void refreshCombinedList() {
        List<Object> combinedList = new ArrayList<>();
        combinedList.add("Contributors:");
        combinedList.addAll(viewModel.getContributorList());
        combinedList.add("\nNotes:");
        combinedList.addAll(viewModel.getNoteList());
        adapter.updateList(combinedList);
    }

    private void showUserDetails(User user) {
        Toast.makeText(this, "User: " + user.getUsername(), Toast.LENGTH_SHORT).show();
    }

    private void showNoteDetails(String note) {
        Toast.makeText(this, "Note: " + note, Toast.LENGTH_SHORT).show();
    }

    private void fetchAndDisplayChartData() {
        String userId = mAuth.getCurrentUser().getUid();

        userDatabaseReference.child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot userSnapshot) {
                        if (userSnapshot.exists()) {
                            String startVacationStr = userSnapshot.child("startVacation").getValue(String.class);
                            String endVacationStr = userSnapshot.child("endVacation").getValue(String.class);

                            if (startVacationStr == null || endVacationStr == null) {
                                Toast.makeText(LogisticsActivity.this, "Vacation dates not set", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Date startVacationDate;
                            Date endVacationDate;
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

                            int vacDur = calculateDaysBetween(startVacationDate, endVacationDate);

                            String tripId = userSnapshot.child("trip").getValue(String.class);
                            if (tripId == null) {
                                Toast.makeText(LogisticsActivity.this, "User's trip not found", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            tripDatabaseReference.child(tripId).child("destinations").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot destinationsSnapshot) {
                                            if (destinationsSnapshot.exists()) {
                                                List<String> destinationIds = new ArrayList<>();
                                                for (DataSnapshot dEntry : destinationsSnapshot.getChildren()) {
                                                    String destinationId = dEntry.getValue(String.class);
                                                    if (destinationId != null) {
                                                        destinationIds.add(destinationId);
                                                    }
                                                }
                                                if (!destinationIds.isEmpty()) {
                                                    fetchDestinationsAndCalculateTotalDuration(destinationIds, vacDur, startVacationDate, endVacationDate);
                                                } else {
                                                    displayPieChart(vacDur, 0);
                                                }
                                            } else {
                                                displayPieChart(vacDur, 0);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(LogisticsActivity.this, "Failed to fetch destinations", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(LogisticsActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LogisticsActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchDestinationsAndCalculateTotalDuration(
            List<String> destinationIds, int vacDur, Date startVacationDate, Date endVacationDate) {
        final int[] destinationsFetched = {0};
        final int totalDestinations = destinationIds.size();
        final int[] totalPlannedDays = {0};
        final int[] overlappingDays = {0};

        if (totalDestinations == 0) {
            displayPieChart(vacDur, 0);
            return;
        }

        for (String destinationId : destinationIds) {
            destinationsDatabaseReference.child(destinationId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot destinationSnapshot) {
                        destinationsFetched[0]++;
                        if (destinationSnapshot.exists()) {
                            Destination dest = destinationSnapshot.getValue(Destination.class);
                            if (dest != null) {
                                int tripDuration = dest.getDuration();
                                totalPlannedDays[0] += tripDuration;
    
                                try {
                                    Date tSDate = dateFormat.parse(dest.getStartDate());
                                    Date tripEndDate = dateFormat.parse(dest.getEndDate());
    
                                    if (tSDate != null && tripEndDate != null) {
                                        int overlap = calculateOverlappingDays(tSDate,
                                                tripEndDate, startVacationDate, endVacationDate);
                                        overlappingDays[0] += overlap;
                                    }
                                } catch (Exception e) {
                                    // error
                                }
                            }
                        }
                        if (destinationsFetched[0] == totalDestinations) {
                            int adjustedPlannedDays = totalPlannedDays[0] - overlappingDays[0];
                            displayPieChart(vacDur + overlappingDays[0], adjustedPlannedDays);
                        }
                    }
    
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        destinationsFetched[0]++;
                        if (destinationsFetched[0] == totalDestinations) {
                            int adjustedPlannedDays = totalPlannedDays[0] - overlappingDays[0];
                            displayPieChart(vacDur + overlappingDays[0], adjustedPlannedDays);
                        }
                    }
                });
        }
    }

    private int calculateDaysBetween(Date startDate, Date endDate) {
        int diffInMillis = (int) (endDate.getTime() - startDate.getTime());
        return (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
    }

    private int calculateOverlappingDays(
            Date tripStart, Date tripEnd, Date vacationStart, Date vacationEnd) {
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
                Toast.makeText(LogisticsActivity.this,
                        "No data to display in chart", Toast.LENGTH_SHORT).show();
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
