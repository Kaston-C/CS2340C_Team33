package com.example.sprintproject.viewmodel;

import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.example.sprintproject.model.MainModel;

public class LoginViewModel {

    public static DatabaseReference firebaseConnect() {
        return MainModel.firebaseConnect("users");
    }
    public static FirebaseAuth firebaseAuthorization() {
        return MainModel.getFirebaseAuthorization();
    }

    public static String getInputUsername(EditText usernameInput) {
        return MainModel.getInputUsername(usernameInput);
    }

    public static String getInputPassword(EditText passwordInput) {
        return MainModel.getInputPassword(passwordInput);
    }
}
