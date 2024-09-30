package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isStartButtonClicked = new MutableLiveData<>();

    public MainViewModel() {
        MutableLiveData<String> welcomeMessage = new MutableLiveData<>();
        welcomeMessage.setValue("Welcome to WanderSync!");
        isStartButtonClicked.setValue(false);
    }

    public LiveData<Boolean> getIsStartButtonClicked() {
        return isStartButtonClicked;
    }

    public void onStartButtonClick() {
        isStartButtonClicked.setValue(true);
    }
}
