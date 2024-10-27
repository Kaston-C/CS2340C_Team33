package com.example.sprintproject.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private final DatabaseReference reference;
    private final FirebaseAuth mAuth;
    private final String databaseName = "users";

    public DatabaseSingleton() {
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference(databaseName);
    }

    public static synchronized DatabaseSingleton getDatabase() {
        if (instance == null) {
            instance = new DatabaseSingleton();
        }
        return instance;
    }
}
