package com.example.sprintproject.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.ActivityDestinationBinding;
import com.example.sprintproject.model.DestinationModel;
import com.example.sprintproject.viewmodel.DestinationViewModel;

import java.util.Calendar;

public class DestinationActivity extends AppCompatActivity implements DestinationViewModel.DatePickerListener {

    private DestinationViewModel viewModel;
    private DestinationAdapter destinationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDestinationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_destination);
        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setDatePickerListener(this);

        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setSelected(true);
        destinationButton.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, DestinationActivity.class);
            startActivity(intent);
        });

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        accomButton.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, AccommodationActivity.class);
            startActivity(intent);
        });

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, CommunityActivity.class);
            startActivity(intent);
        });

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, Transportation.class);
            startActivity(intent);
        });

        setupRecyclerView(binding);
    }

    private void setupRecyclerView(ActivityDestinationBinding binding) {
        destinationAdapter = new DestinationAdapter(viewModel.destinationsList, destination -> {
            showDestinationDetails(destination);
        });
        binding.destinationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.destinationsRecyclerView.setAdapter(destinationAdapter);

        viewModel.destinationsList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<DestinationModel>>() {
            public void onChanged(ObservableList<DestinationModel> sender) {
                destinationAdapter.notifyDataSetChanged();
            }

            public void onItemRangeChanged(ObservableList<DestinationModel> sender, int positionStart, int itemCount) {
                destinationAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeInserted(ObservableList<DestinationModel> sender, int positionStart, int itemCount) {
                destinationAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeMoved(ObservableList<DestinationModel> sender, int fromPosition, int toPosition, int itemCount) {
                destinationAdapter.notifyDataSetChanged();
            }

            public void onItemRangeRemoved(ObservableList<DestinationModel> sender, int positionStart, int itemCount) {
                destinationAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    private void showDestinationDetails(DestinationModel destination) {
        Toast.makeText(this, "Destination: " + destination.getLocation(), Toast.LENGTH_SHORT).show();
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
}
