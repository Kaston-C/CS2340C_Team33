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

public class RegistrationActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private EditText username_input;
    private EditText password_input;
    private Button register_button;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        username_input = findViewById(R.id.input_username);
        password_input = findViewById(R.id.input_password);
        register_button = findViewById(R.id.register_button);

        register_button.setOnClickListener(view -> {
            username = username_input.getText().toString();
            password = password_input.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                databaseReference.child(username).setValue(password);
                Intent go_login = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(go_login);
            } else {
                username_input.setError("Please enter a username");
                password_input.setError("Please enter a password");
            }
        });

        Button login_button = findViewById(R.id.go_back_login_button);
        login_button.setOnClickListener(view -> {
            Intent go_login = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(go_login);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}