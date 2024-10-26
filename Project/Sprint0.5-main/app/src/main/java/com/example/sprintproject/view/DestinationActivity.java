package com.example.sprintproject.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.ActivityDestinationBinding;
import com.example.sprintproject.viewmodel.DestinationViewModel;

import java.util.Calendar;

public class DestinationActivity extends AppCompatActivity implements DestinationViewModel.DatePickerListener {

    private DestinationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDestinationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_destination);
        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setDatePickerListener(this);
        setupNavigationButtons();
    }

    @Override
    public void onStartDateClick() {
        showDatePickerDialog(date -> viewModel.startDate.set(date));
    }

    @Override
    public void onEndDateClick() {
        showDatePickerDialog(date -> viewModel.endDate.set(date));
    }

    private void showDatePickerDialog(OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
            listener.onDateSet(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private interface OnDateSetListener {
        void onDateSet(String date);
    }
    private void setupNavigationButtons() {
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> navigateToActivity(LogisticsActivity.class));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setSelected(true);
        destinationButton.setOnClickListener(v -> navigateToActivity(DestinationActivity.class));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> navigateToActivity(DiningActivity.class));

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        accomButton.setOnClickListener(v -> navigateToActivity(AccommodationActivity.class));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> navigateToActivity(CommunityActivity.class));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> navigateToActivity(Transportation.class));
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(DestinationActivity.this, activityClass);
        startActivity(intent);
    }
}
