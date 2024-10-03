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

public class LoginActivity extends AppCompatActivity {

    EditText username_input;
    EditText password_input;
    Button submit_user_pass;
    Button register_button;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        username_input = findViewById(R.id.input_username);
        password_input = findViewById(R.id.input_password);

        submit_user_pass = findViewById(R.id.submit_button);

        submit_user_pass.setOnClickListener(view -> {
           String username = username_input.getText().toString();
           String password = password_input.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                    Intent go_home = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(go_home);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                username_input.setError("Please enter a username.");
                password_input.setError("Please enter a password.");
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