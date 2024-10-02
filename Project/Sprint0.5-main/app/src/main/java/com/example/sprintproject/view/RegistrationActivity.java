package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;

public class RegistrationActivity extends AppCompatActivity {


    String username;
    String password;

    EditText username_input;
    EditText password_input;
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        username_input = findViewById(R.id.input_username);
        password_input = findViewById(R.id.input_password);

        register_button = findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = username_input.getText().toString();
                password = password_input.getText().toString();
            }
        });

        Button login_button = findViewById(R.id.go_back_login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_login = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(go_login);
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}