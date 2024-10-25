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
import com.example.sprintproject.viewmodel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class RegistrationActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText usernameInput;
    private EditText passwordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //set view to account creation screen
        setContentView(R.layout.activity_registration);

        //connect firebase database and firebase authentication
        databaseReference = LoginViewModel.firebaseConnect();
        mAuth = LoginViewModel.firebaseAuthorization();

        //set up username and password fields
        usernameInput = findViewById(R.id.input_username);
        passwordInput = findViewById(R.id.input_password);

        //set up account registration button
        Button registerButton = findViewById(R.id.registerButton);

        //check for click of account creation button to create account
        registerButton.setOnClickListener(view -> {
            //set the username and password strings to the current entered values
            String username = LoginViewModel.getInputUsername(usernameInput);
            String password = LoginViewModel.getInputPassword(passwordInput);

            //check if the inputs are valid
            if (!username.isBlank() && !password.isBlank()) {
                //attempt to create account in firebase authentication
                mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(
                        task -> {
                            //check if account properly created
                            if (task.isSuccessful()) {
                                //set user to the current firebase auth user
                                FirebaseUser user = mAuth.getCurrentUser();
                                // log in user if account properly created
                                if (user != null) {
                                    databaseReference.child(user.getUid()).setValue(username);
                                    Intent goLogin = new Intent(
                                            RegistrationActivity.this, LoginActivity.class);
                                    startActivity(goLogin);
                                }
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                //invalid inputs for username/password
                if (username.isBlank() && password.isBlank()) {
                    usernameInput.setError("Please enter a username.");
                } else if (username.isBlank()) {
                    usernameInput.setError("Please enter a username.");
                } else {
                    passwordInput.setError("Please enter a password.");
                }
            }
        });

        //create button to return to log in
        Button loginButton = findViewById(R.id.go_back_login_button);
        loginButton.setOnClickListener(view -> {
            Intent goLogin = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(goLogin);
        });

        //create window insets if needed
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}