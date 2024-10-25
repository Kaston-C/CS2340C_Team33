package com.example.sprintproject.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

import java.util.Calendar;

public class DestinationActivity extends AppCompatActivity {

    int start_day, start_month, start_year, end_day, end_month, end_year, duration;
    boolean start_select, end_select, duration_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set current view to destination activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        start_select = false;
        end_select = false;
        duration_select = false;

        //set menu buttons to switch current screen
        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(v -> startActivity(new Intent(DestinationActivity.this, LogisticsActivity.class)));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setSelected(true);
        destinationButton.setOnClickListener(v -> startActivity(new Intent(DestinationActivity.this, DestinationActivity.class)));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(v -> startActivity(new Intent(DestinationActivity.this, DiningActivity.class)));

        ImageButton accomButton = findViewById(R.id.accommodation_button);
        accomButton.setOnClickListener(v -> startActivity(new Intent(DestinationActivity.this, AccommodationActivity.class)));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setOnClickListener(v -> startActivity(new Intent(DestinationActivity.this, CommunityActivity.class)));

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(v -> startActivity(new Intent(DestinationActivity.this, Transportation.class)));

        Button startDateButton = findViewById(R.id.begin_date_button);
        TextView startDate = findViewById(R.id.begin_date_field);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateSelect = new DatePickerDialog(DestinationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        start_day = i2;
                        start_month = i1;
                        start_year = i;
                        start_select = true;
                        startDate.setText(start_month + "/" + start_day + "/" + start_year);
                    }
                }, year, month, day);
                dateSelect.show();
            }
        });

        TextView endDate = findViewById(R.id.end_date_field);
        Button endDateButton = findViewById(R.id.end_date_button);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateSelect = new DatePickerDialog(DestinationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        end_day = i2;
                        end_month = i1;
                        end_year = i;
                        end_select = true;
                        endDate.setText(end_month + "/" + end_day + "/" + end_year);
                    }
                }, year, month, day);
                dateSelect.show();
            }
        });

        EditText durationText = findViewById(R.id.duration_field);

        Button submitDestination = findViewById(R.id.submit_trip_details_button);
        submitDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duration_select = !durationText.getText().toString().isBlank();
                try {
                    if (duration_select) duration = Integer.parseInt(durationText.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(DestinationActivity.this, "Invalid Duration", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}