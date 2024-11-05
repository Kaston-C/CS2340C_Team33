package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LogisticsViewModel extends AndroidViewModel {
    private ObservableField<String> username = new ObservableField<>("");
    private ObservableField<String> note = new ObservableField<>("");
    private ObservableArrayList<User> contributorList = new ObservableArrayList<>();

    public ObservableField<String> getLocation() {
        return username;
    }
    public ObservableField<String> getStartDate() {
        return note;
    }
    public ObservableArrayList<User> getDestinationsList() {
        return contributorList;
    }

    private DatabaseSingleton db;
    private DatabaseReference tripDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth mAuth;

    public LogisticsViewModel(Application application) {
        super(application);
        db = DatabaseSingleton.getDatabase();
        mAuth = db.getFirebaseAuthorization();
        tripDatabaseReference = db.tripDb();
        userDatabaseReference = db.userDb();
        loadContributors();
    }

    public void onSubmitContributor(String contributor) {
        String contributorKey = getUserKey(contributor);
        String userId = mAuth.getCurrentUser().getUid();

        userDatabaseReference.child(userId).child("trip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tripId = dataSnapshot.getValue(String.class);
                    tripDatabaseReference.child(tripId).child("users").child(contributorKey).setValue(username.get());

                    userDatabaseReference.child(contributorKey).setValue(tripId);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String getUserKey(String contributorName) {
        final String[] toReturn = new String[1];
        Query query = userDatabaseReference.orderByChild("username").equalTo(username.get());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        toReturn[0] = childSnapshot.getKey().toString();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return toReturn[0];
    }

    public void onSubmitNote(View view) {
        String userId = mAuth.getCurrentUser().getUid();
        userDatabaseReference.child(userId).child("trip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tripId = dataSnapshot.getValue(String.class);

                    tripDatabaseReference.child(tripId).child("notes").setValue(note.get());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadContributors() {
        String userId = mAuth.getCurrentUser().getUid();
        userDatabaseReference.child(userId).child("trip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tripId = dataSnapshot.getValue(String.class);

                    tripDatabaseReference.child(tripId).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot userSnapshot) {
                            if (userSnapshot.exists()) {
                                ArrayList<User> tempContributors = new ArrayList<>();
                                int totalContributors = (int) userSnapshot.getChildrenCount();
                                if (totalContributors == 0) {
                                    contributorList.clear();
                                }
                                int[] loadedCount = {0};
                                for (DataSnapshot dataSnapshot : userSnapshot.getChildren()) {
                                    String contributorId = dataSnapshot.getValue(String.class);
                                    loadContributorById(contributorId,
                                            tempContributors, totalContributors, loadedCount);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplication(),
                                    "Failed to load destination", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplication(),
                        "Failed to load contributors", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadContributorById(String contributorId, List<User> tempContributor,
                                     int totalContributors, int[] loadedCount) {
        userDatabaseReference.child(contributorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User contributor = snapshot.getValue(User.class);
                        if (contributor != null) {
                            tempContributor.add(contributor);
                        }
                        loadedCount[0]++;
                        if (loadedCount[0] == totalContributors) {
                            contributorList.clear();
                            contributorList.addAll(tempContributor);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getApplication(),
                                "Failed to load contributors", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}