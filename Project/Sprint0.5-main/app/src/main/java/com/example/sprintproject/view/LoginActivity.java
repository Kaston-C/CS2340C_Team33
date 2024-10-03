package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class LoginActivity extends AppCompatActivity {

    EditText username_input;
    EditText password_input;
    Button submit_user_pass;
    Button register_button;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        username_input = findViewById(R.id.input_username);
        password_input = findViewById(R.id.input_password);

        submit_user_pass = findViewById(R.id.submit_button);
        submit_user_pass.setOnClickListener(view -> {
           String username = username_input.getText().toString();
           String password = password_input.getText().toString();

           if (!username.isEmpty() && !password.isEmpty()) {
               databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()) {
                           String stored_password = dataSnapshot.getValue(String.class);
                           if (stored_password.equals(password)) {
                               Intent go_home = new Intent(LoginActivity.this, MainActivity.class);
                               startActivity(go_home);
                           } else {
                               password_input.setError("Incorrect password");
                           }
                       } else {
                           username_input.setError("Username not found");
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError error) {
                       username_input.setError("Error connecting to database");
                   }
               });
           } else {
               username_input.setError("Please enter a username");
               password_input.setError("Please enter a password");
           }
        });

        register_button = findViewById(R.id.go_register_button);
        register_button.setOnClickListener(view -> {
            Intent go_register = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(go_register);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}