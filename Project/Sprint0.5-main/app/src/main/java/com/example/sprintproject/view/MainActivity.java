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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.onwardsButton.setOnClickListener(v -> {
            mainViewModel.onStartButtonClick();
        });
    }
}