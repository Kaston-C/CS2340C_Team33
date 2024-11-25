package com.example.sprintproject.view;

import static com.example.sprintproject.model.Accommodation.isCheckOutDateValid;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelPost;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {

    private EditText inputDestination;
    private EditText inputStartDate;
    private EditText inputEndDate;
    private EditText inputAccommodation;
    private EditText inputDining;
    private EditText inputTransportation;
    private EditText inputNotes;

    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        inputDestination = findViewById(R.id.inputDestination);
        inputStartDate = findViewById(R.id.inputStartDate);
        inputEndDate = findViewById(R.id.inputEndDate);
        inputAccommodation = findViewById(R.id.inputAccommodation);
        inputDining = findViewById(R.id.inputDining);
        inputTransportation = findViewById(R.id.inputTransportation);
        inputNotes = findViewById(R.id.inputNotes);
        submitButton = findViewById(R.id.submitButton);

        DatabaseReference postRef = FirebaseDatabase.getInstance()
                .getReference("communityTravelPosts");

        submitButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String destination = inputDestination.getText().toString();
                String startDate = inputStartDate.getText().toString();
                String endDate = inputEndDate.getText().toString();
                String accommodation = inputAccommodation.getText().toString();
                String dining = inputDining.getText().toString();
                String transportation = inputTransportation.getText().toString();
                String notes = inputNotes.getText().toString();

                TravelPost newPost = new TravelPost(
                        destination,
                        startDate, endDate, accommodation, dining, transportation, notes);
                postRef.push().setValue(newPost);

                Toast.makeText(this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean validateInputs() {
        String destination = inputDestination.getText().toString().trim();
        String startDate = inputStartDate.getText().toString().trim();
        String endDate = inputEndDate.getText().toString().trim();

        if (TextUtils.isEmpty(destination)) {
            showToast("Destination should not be empty.");
            return false;
        }

        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            showToast("Need to enter start and end .");
            return false;
        }

        if (!isCheckOutDateValid(startDate, endDate)) {
            showToast("The start date has to be before end date.");
            return false;
        }

        return true;
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
