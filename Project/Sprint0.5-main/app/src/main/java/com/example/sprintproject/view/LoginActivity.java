package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.sprintproject.R;
import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.viewmodel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * The LoginActivity class extends AppCompatActivity.
 * This class is part of the ViewModel folder within the MVVM architecture.
 * The class handles username and password inputs and validation.
 * It also helps with Firebase authentication and connection in tandem with Model.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //set view to login screen
        setContentView(R.layout.activity_login);
        DatabaseReference databaseReference = DatabaseSingleton.getDatabase().userDb();
        FirebaseAuth mAuth = DatabaseSingleton.getDatabase().getFirebaseAuthorization();

        //set up username and password fields
        EditText usernameInput = findViewById(R.id.input_username);
        EditText passwordInput = findViewById(R.id.input_password);

        //set up login submit button
        Button submitUserPass = findViewById(R.id.submit_button);

        //log in verification process
        submitUserPass.setOnClickListener(view -> {
            //set the values of the username and password according to entered values
            String username = LoginViewModel.getInputUsername(usernameInput);
            String password = LoginViewModel.getInputPassword(passwordInput);

            //check if the inputs are valid
            int length = Toast.LENGTH_SHORT;
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
                            Toast.makeText(LoginActivity.this, "Login successful.", length).show();
                            Intent goHome = new Intent(LoginActivity.this, TripActivity.class);
                            startActivity(goHome);
                        }
                    } else {
                        //username and/or password invalid
                        Toast.makeText(LoginActivity.this, "Login failed.", length).show();
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

        //set up button to account creation screen
        Button registerButton = findViewById(R.id.goRegisterButton);
        registerButton.setOnClickListener(view -> {
            Intent goRegister = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(goRegister);
        });

        View exitButton = findViewById(R.id.go_exitButton);
        exitButton.setOnClickListener(view ->
            finishAffinity());

        //create window insets if needed
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}