package com.example.sprintproject.model;
import android.widget.EditText;
import com.example.sprintproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainModel {
    private String welcomeMessage;

    public MainModel() {
        this.welcomeMessage = "Welcome to WanderSync!";
    }

    public String getWelcomeMessage() {
        return this.welcomeMessage;
    }

    public void setWelcomeMessage(String message) {
        this.welcomeMessage = message;
    }

    public static DatabaseReference firebaseConnect() {
        return FirebaseDatabase.getInstance().getReference("users");
    }
    public static FirebaseAuth getFirebaseAuthorization() {
        return FirebaseAuth.getInstance();
    }

    public static String getInputUsername(EditText usernameInput) {
        //concatenate @wandersync.com if no email used
        String username = usernameInput.getText().toString();
        if (!username.contains("@") && !username.contains(".")) {
            username += "@wandersync.com";
        }
        return username;
    }

    public static String getInputPassword(EditText passwordInput) {
        return passwordInput.getText().toString();
    }

}
