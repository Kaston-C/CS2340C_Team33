package com.example.sprintproject.viewmodel;

import android.widget.EditText;
import com.example.sprintproject.model.MainModel;

public class LoginViewModel {
    public static String getInputUsername(EditText usernameInput) {
        return MainModel.getInputUsername(usernameInput);
    }

    public static String getInputPassword(EditText passwordInput) {
        return MainModel.getInputPassword(passwordInput);
    }
}
