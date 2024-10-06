package com.example.sprintproject.viewmodel;

import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.example.sprintproject.model.MainModel;

public class LoginViewModel {

    public static DatabaseReference firebaseConnect() {
        return MainModel.firebaseConnect();
    }
    public static FirebaseAuth firebaseAuthorization() {
        return MainModel.getFirebaseAuthorization();
    }

    public static String getInputUsername(EditText username_input) {
        return MainModel.getInputUsername(username_input);
    }

    public static String getInputPassword(EditText password_input) {
        return MainModel.getInputPassword(password_input);
    }
}
