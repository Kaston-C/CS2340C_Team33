package com.example.sprintproject.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseSingleton {
    private static DatabaseSingleton instance;

    public DatabaseSingleton() {
        // empty constructor for singleton
    }

    public static synchronized DatabaseSingleton getDatabase() {
        if (instance == null) {
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    public FirebaseAuth getFirebaseAuthorization() {
        return FirebaseAuth.getInstance();
    }

    public DatabaseReference userDb() {
        return FirebaseDatabase.getInstance().getReference("users");
    }

    public DatabaseReference destinationsDb() {
        return FirebaseDatabase.getInstance().getReference("destinations");
    }

    public DatabaseReference tripDb() {
        return FirebaseDatabase.getInstance().getReference("trips");
    }

    public DatabaseReference communityDb() {
        return FirebaseDatabase.getInstance().getReference("community");
    }
}
