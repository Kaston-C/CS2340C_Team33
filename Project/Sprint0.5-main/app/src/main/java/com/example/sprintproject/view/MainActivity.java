package com.example.sprintproject.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.databinding.ActivityMainBinding;  // Check this import
import com.example.sprintproject.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ViewModel initialization
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Set up any view actions using binding
        binding.onwardsButton.setOnClickListener(v -> {
            mainViewModel.onStartButtonClick();
        });
    }
}
