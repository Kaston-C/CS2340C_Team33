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

    private EditText username_input;
    private EditText password_input;
    private Button submit_user_pass;
    private Button register_button;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //set view to login screen
        setContentView(R.layout.activity_login);

        //connect firebase database and firebase authentication
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        //set up username and password fields
        username_input = findViewById(R.id.input_username);
        password_input = findViewById(R.id.input_password);

        //set up login submit button
        submit_user_pass = findViewById(R.id.submit_button);

        //log in verification process
        submit_user_pass.setOnClickListener(view -> {
            //set the values of the username and password according to entered values
           String username = username_input.getText().toString();
            if (!username.contains("@") && !username.contains(".")) {
                username += "@wandersync.com";
            }
           String password = password_input.getText().toString();

           //check if the inputs are valid
            if (!username.isBlank() && !password.isBlank()) {
                //send the inputs to the firebase authentication to determine if valid account
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                            //check if log in occurs
                            if (task.isSuccessful()) {
                                //set user to current user
                                FirebaseUser user = mAuth.getCurrentUser();
                                //check value of user to determine if a valid user is found
                                if (user != null) {
                                    //log in to account
                                    Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                    Intent go_home = new Intent(LoginActivity.this, LogisticsActivity.class);
                                    startActivity(go_home);
                                }
                            } else {
                                //username and/or password invalid
                                Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                //invalid inputs for username/password
                username_input.setError("Please enter a username.");
                password_input.setError("Please enter a password.");
            }
        });

        //set up button to account creation screen
        register_button = findViewById(R.id.go_register_button);
        register_button.setOnClickListener(view -> {
            Intent go_register = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(go_register);
        });

        //create window insets if needed
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}