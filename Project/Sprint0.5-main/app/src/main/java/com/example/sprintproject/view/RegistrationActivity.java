package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText username_input;
    private EditText password_input;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        username_input = findViewById(R.id.input_username);
        password_input = findViewById(R.id.input_password);
        Button register_button = findViewById(R.id.register_button);

        register_button.setOnClickListener(view -> {
            String username = username_input.getText().toString();
            String password = password_input.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            databaseReference.child(user.getUid()).setValue(username);
                            Intent go_login = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(go_login);
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                if (username.isEmpty()) {
                    username_input.setError("Please enter a username.");
                }
                if (password.isEmpty()) {
                    password_input.setError("Please enter a password.");
                }
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