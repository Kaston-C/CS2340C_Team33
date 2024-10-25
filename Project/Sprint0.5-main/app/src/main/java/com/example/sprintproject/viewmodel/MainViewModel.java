package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isStartButtonClicked = new MutableLiveData<>();
    //declare the firebase authentication
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    public MainViewModel() {
        MutableLiveData<String> welcomeMessage = new MutableLiveData<>();
        welcomeMessage.setValue("Welcome to WanderSync!");
        isStartButtonClicked.setValue(false);

        //instantiate firebase and firebase authentication
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public LiveData<Boolean> getIsStartButtonClicked() {
        return isStartButtonClicked;
    }

    public void onStartButtonClick() {
        isStartButtonClicked.setValue(true);
    }

    public void setDate(int day, int month, int year) {

    }
}