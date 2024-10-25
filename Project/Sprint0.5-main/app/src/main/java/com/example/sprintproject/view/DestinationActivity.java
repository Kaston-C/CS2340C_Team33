package com.example.sprintproject.view;

import static java.time.LocalDate.of;

import android.annotation.SuppressLint;
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

import java.time.LocalDate;
import java.util.Calendar;

public class DestinationActivity extends AppCompatActivity {

    int duration;
    String end_string, start_string;
    Calendar startSel, endSel;
    boolean start_select, end_select, duration_select;
    int end_doy, start_doy, end_year, start_year;

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
                        startSel = Calendar.getInstance();
                        startSel.set(i, i1, i2);
                        start_doy = startSel.get(Calendar.DAY_OF_YEAR);
                        start_year = startSel.get(Calendar.YEAR);
                        start_string = i1 + "/" + i2 + "/" + i;
                        start_select = true;
                        startDate.setText(start_string);
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
                        endSel = Calendar.getInstance();
                        endSel.set(i, i1, i2);
                        end_doy = endSel.get(Calendar.DAY_OF_YEAR);
                        end_year = endSel.get(Calendar.YEAR);
                        end_string = i1 + "/" + i2 + "/" + i;
                        end_select = true;
                        endDate.setText(end_string);
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
                try {
                    duration_select = !durationText.getText().toString().isBlank();
                    if (duration_select) duration = Integer.parseInt(durationText.getText().toString());
                    if (duration_select && end_select && start_select) {
                        Toast.makeText(DestinationActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        if (end_select && start_select) {
                            if ((end_year - start_year) * 365 + (end_doy - start_doy) < 0) {
                                Toast.makeText(DestinationActivity.this, "End Date Before Start", Toast.LENGTH_SHORT).show();
                            } else {
                                duration = (end_year - start_year) * 365 + (end_doy - start_doy) + 1;
                                durationText.setText(String.format("%d", duration));
                            }
                        } else if (duration_select && start_select) {
                            endSel = (Calendar) startSel.clone();
                            endSel.add(Calendar.DAY_OF_YEAR, duration - 1);
                            end_string = endSel.get(Calendar.MONTH) + "/" + endSel.get(Calendar.DAY_OF_MONTH) + "/" + endSel.get(Calendar.YEAR);
                            endDate.setText(end_string);
                        } else if (duration_select && end_select) {
                            startSel = (Calendar) endSel.clone();
                            startSel.add(Calendar.DAY_OF_YEAR, -duration + 1);
                            start_string = startSel.get(Calendar.MONTH) + "/" + startSel.get(Calendar.DAY_OF_MONTH) + "/" + startSel.get(Calendar.YEAR);
                            startDate.setText(start_string);
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(DestinationActivity.this, "Invalid Duration", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}