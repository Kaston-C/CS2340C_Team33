package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.model.Destination;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DestinationViewModel extends AndroidViewModel {
    private ObservableField<String> location = new ObservableField<>("");
    private ObservableField<String> startDate = new ObservableField<>("");
    private ObservableField<String> endDate = new ObservableField<>("");
    private ObservableField<String> duration = new ObservableField<>("");
    private ObservableField<String> totalDaysPlanned = new ObservableField<>("0 days planned");
    private ObservableBoolean showInputs = new ObservableBoolean(false);
    private ObservableBoolean showVacationInputs = new ObservableBoolean(false);
    private ObservableArrayList<Destination> destinationsList = new ObservableArrayList<>();

    public ObservableField<String> getLocation() {
        return location;
    }
    public ObservableField<String> getStartDate() {
        return startDate;
    }
    public ObservableField<String> getEndDate() {
        return endDate;
    }
    public ObservableField<String> getDuration() {
        return duration;
    }
    public ObservableField<String> getTotalDaysPlanned() {
        return totalDaysPlanned;
    }
    public ObservableBoolean getShowInputs() {
        return showInputs;
    }
    public ObservableBoolean getShowVacationInputs() {
        return showVacationInputs;
    }
    public ObservableArrayList<Destination> getDestinationsList() {
        return destinationsList;
    }

    private DatePickerListener datePickerListener;
    private DatabaseReference tripDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference destinationsDatabaseReference;
    private FirebaseAuth mAuth;
    private DatabaseSingleton db;

    public DestinationViewModel(Application application) {
        super(application);
        db = DatabaseSingleton.getDatabase();
        mAuth = db.getFirebaseAuthorization();
        tripDatabaseReference = db.tripDb();
        destinationsDatabaseReference = db.destinationsDb();
        userDatabaseReference = db.userDb();
        loadDestinations();
    }

    public void setDatePickerListener(DatePickerListener listener) {
        this.datePickerListener = listener;
    }

    public void onLogTravelClicked(View view) {
        showInputs.set(true);
        showVacationInputs.set(false);
    }

    public void onLogVacationClicked(View view) {
        showVacationInputs.set(true);
        showInputs.set(false);
    }

    public void onCancelClicked(View view) {
        showInputs.set(false);
        location.set("");
        startDate.set("");
        endDate.set("");
        duration.set("");
    }

    public void onCancelVacationClicked(View view) {
        showVacationInputs.set(false);
        startDate.set("");
        endDate.set("");
        duration.set("");
    }

    public void onStartDateClicked(View view) {
        if (datePickerListener != null) {
            datePickerListener.onStartDateClick();
        }
    }

    public void onEndDateClicked(View view) {
        if (datePickerListener != null) {
            datePickerListener.onEndDateClick();
        }
    }

    public void onCalculateDuration(View view) {
        if (!startDate.get().isEmpty() && !endDate.get().isEmpty()) {
            int calculatedDuration = calculateDurationInDays(startDate.get(), endDate.get());
            if (calculatedDuration >= 0) {
                duration.set(String.valueOf(calculatedDuration));
                Toast.makeText(getApplication(), "Trip Duration: "
                        + calculatedDuration + " days", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "End date must be after start date",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (!startDate.get().isEmpty() && !duration.get().isEmpty()) {
            String calculatedEndDate = calculateEndDate(startDate.get(),
                    Integer.parseInt(duration.get()));
            endDate.set(calculatedEndDate);
            Toast.makeText(getApplication(), "Calculated End Date: "
                    + calculatedEndDate, Toast.LENGTH_SHORT).show();
        } else if (!endDate.get().isEmpty() && !duration.get().isEmpty()) {
            String calculatedStartDate = calculateStartDate(endDate.get(),
                    Integer.parseInt(duration.get()));
            startDate.set(calculatedStartDate);
            Toast.makeText(getApplication(), "Calculated Start Date: "
                    + calculatedStartDate, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "You must provide at least two inputs",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onSubmitDestination(View view) {
        String userId = mAuth.getCurrentUser().getUid();
        String destinationId = UUID.randomUUID().toString();

        if (location.get().isEmpty()
                || startDate.get().isEmpty()
                || endDate.get().isEmpty() || duration.get().isEmpty()) {
            Toast.makeText(getApplication(),
                    "You need to fill all fields before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }

        Destination destination = new Destination(
                location.get(),
                startDate.get(),
                endDate.get(),
                Integer.parseInt(duration.get())
        );

        destinationsDatabaseReference.child(destinationId).setValue(destination)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userDatabaseReference.child(userId).child("trip")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String tripId = dataSnapshot.getValue(String.class);
                                            tripDatabaseReference.child(tripId)
                                                    .child("destinations")
                                                    .child(destination.getLocation())
                                                    .setValue(destinationId)
                                                    .addOnCompleteListener(task -> {
                                                    });
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
    }

    public void onSubmitVacation(View view) {
        String userId = mAuth.getCurrentUser().getUid();

        if (startDate.get().isEmpty() || endDate.get().isEmpty() || duration.get().isEmpty()) {
            Toast.makeText(getApplication(),
                    "You need to fill all fields before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }

        userDatabaseReference.child(userId).child("startVacation")
                .setValue(startDate.get())
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(),
                            "Failed to save vacation", Toast.LENGTH_SHORT).show();
                });

        userDatabaseReference.child(userId).child("endVacation")
                .setValue(endDate.get())
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(),
                            "Failed to save vacation", Toast.LENGTH_SHORT).show();
                });

        userDatabaseReference.child(userId).child("vacationDuration")
                .setValue(Integer.parseInt(duration.get()))
                .addOnSuccessListener(aVoid -> {
                    startDate.set("");
                    endDate.set("");
                    duration.set("");
                    showVacationInputs.set(false);
                    loadDestinations();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(),
                            "Failed to save vacation", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadDestinations() {
        String userId = mAuth.getCurrentUser().getUid();
        userDatabaseReference.child(userId).child("trip")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String tripId = dataSnapshot.getValue(String.class);

                            tripDatabaseReference.child(tripId).child("destinations")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot destinationSnapshot) {
                                            if (destinationSnapshot.exists()) {
                                                ArrayList<Destination> tempDestinations
                                                        = new ArrayList<>();
                                                int totalDestinations
                                                        = (int) destinationSnapshot
                                                        .getChildrenCount();
                                                if (totalDestinations == 0) {
                                                    destinationsList.clear();
                                                }
                                                int[] loadedCount = {0};
                                                for (DataSnapshot dataSnapshot
                                                        : destinationSnapshot.getChildren()) {
                                                    String destinationId
                                                            = dataSnapshot.getValue(String.class);
                                                    loadDestinationById(destinationId,
                                                            tempDestinations, totalDestinations,
                                                            loadedCount);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplication(),
                                                    "Failed to load destination",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplication(),
                                "Failed to load destination", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadDestinationById(String destinationId, List<Destination> tempDestinations,
                                     int totalDestinations, int[] loadedCount) {
        destinationsDatabaseReference.child(destinationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        Destination destination = snapshot.getValue(Destination.class);
                        if (destination != null) {
                            tempDestinations.add(destination);
                        }
                        loadedCount[0]++;
                        if (loadedCount[0] == totalDestinations) {
                            destinationsList.clear();
                            destinationsList.addAll(tempDestinations);
                        }
                    }
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getApplication(),
                                "Failed to load destination", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private int calculateDurationInDays(String startDateStr, String endDateStr) {
        int diffInDays;
        diffInDays = Destination.calculateDurationInDays(startDateStr, endDateStr);
        if (diffInDays < 0) {
            Toast.makeText(getApplication(), "Invalid date format", Toast.LENGTH_SHORT).show();
        }
        return diffInDays;
    }

    private String calculateEndDate(String startDateStr, int durationDays) {
        String endDateStr = Destination.calculateEndDate(startDateStr, durationDays);
        if (endDateStr == null) {
            Toast.makeText(getApplication(),
                    "Invalid start date format", Toast.LENGTH_SHORT).show();
            return "";
        }
        return endDateStr;
    }

    private String calculateStartDate(String endDateStr, int durationDays) {
        String startDateStr = Destination.calculateStartDate(endDateStr, durationDays);
        if (startDateStr == null) {
            Toast.makeText(getApplication(), "Invalid end date format", Toast.LENGTH_SHORT).show();
            return "";
        }
        return startDateStr;
    }

    public interface DatePickerListener {
        void onStartDateClick();
        void onEndDateClick();
    }
}