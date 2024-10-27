package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.sprintproject.model.DestinationModel;
import com.example.sprintproject.model.MainModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.UUID;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DestinationViewModel extends AndroidViewModel {
    public ObservableField<String> location = new ObservableField<>("");
    public ObservableField<String> startDate = new ObservableField<>("");
    public ObservableField<String> endDate = new ObservableField<>("");
    public ObservableField<String> duration = new ObservableField<>("");
    public ObservableBoolean showInputs = new ObservableBoolean(false);
    public ObservableArrayList<DestinationModel> destinationsList = new ObservableArrayList<>();

    private DatePickerListener datePickerListener;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private DestinationModel model;

    public DestinationViewModel(Application application) {
        super(application);
        mAuth = MainModel.getFirebaseAuthorization();
        databaseReference = MainModel.firebaseConnect("users");
        loadDestinations();
    }

    public void setDatePickerListener(DatePickerListener listener) {
        this.datePickerListener = listener;
    }

    public void onLogTravelClicked(View view) {
        showInputs.set(true);
    }

    public void onCancelClicked(View view) {
        showInputs.set(false);
        location.set("");
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
                Toast.makeText(getApplication(), "Trip Duration: " + calculatedDuration + " days", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "End date must be after start date", Toast.LENGTH_SHORT).show();
            }
        } else if (!startDate.get().isEmpty() && !duration.get().isEmpty()) {
            String calculatedEndDate = calculateEndDate(startDate.get(), Integer.parseInt(duration.get()));
            endDate.set(calculatedEndDate);
            Toast.makeText(getApplication(), "Calculated End Date: " + calculatedEndDate, Toast.LENGTH_SHORT).show();
        } else if (!endDate.get().isEmpty() && !duration.get().isEmpty()) {
            String calculatedStartDate = calculateStartDate(endDate.get(), Integer.parseInt(duration.get()));
            startDate.set(calculatedStartDate);
            Toast.makeText(getApplication(), "Calculated Start Date: " + calculatedStartDate, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "You must provide at least two inputs", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSubmitDestination(View view) {
        String userId = mAuth.getCurrentUser().getUid();
        String destinationId = UUID.randomUUID().toString();

        if (location.get().isEmpty() || startDate.get().isEmpty() || endDate.get().isEmpty() || duration.get().isEmpty()) {
            Toast.makeText(getApplication(), "You need to fill all fields before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }

        DestinationModel destination = new DestinationModel(
                destinationId,
                location.get(),
                startDate.get(),
                endDate.get(),
                Integer.parseInt(duration.get())
        );

        databaseReference.child(userId).child("destinations").child(destinationId)
                .setValue(destination)
                .addOnSuccessListener(aVoid -> {
                    location.set("");
                    startDate.set("");
                    endDate.set("");
                    duration.set("");
                    showInputs.set(false);
                    loadDestinations();
                });
    }

    private void loadDestinations() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.child(userId).child("destinations")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        destinationsList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            DestinationModel destination = dataSnapshot.getValue(DestinationModel.class);
                            destinationsList.add(destination);
                        }
                    }

                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getApplication(), "Failed to load destinations", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int calculateDurationInDays(String startDateStr, String endDateStr) {
        int diffInDays;
        diffInDays = DestinationModel.calculateDurationInDays(startDateStr, endDateStr);
        if (diffInDays < 0) {
            Toast.makeText(getApplication(), "Invalid date format", Toast.LENGTH_SHORT).show();
        }
        return diffInDays;
    }

    private String calculateEndDate(String startDateStr, int durationDays) {
        String endDateStr = DestinationModel.calculateEndDate(startDateStr, durationDays);
        if (endDateStr == null) {
            Toast.makeText(getApplication(), "Invalid start date format", Toast.LENGTH_SHORT).show();
            return "";
        }
        return endDateStr;
    }

    private String calculateStartDate(String endDateStr, int durationDays) {
        String startDateStr = DestinationModel.calculateStartDate(endDateStr, durationDays);
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
