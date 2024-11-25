package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;

import com.example.sprintproject.model.CurrentTrip;
import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LogisticsViewModel extends AndroidViewModel {
    private ObservableArrayList<String> noteList = new ObservableArrayList<>();
    private ObservableArrayList<User> contributorList = new ObservableArrayList<>();

    public ObservableArrayList<String> getNoteList() {
        return noteList; }
    public ObservableArrayList<User> getContributorList() {
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
        loadNotes();
    }

    public void onSubmitContributor(String contributorName) {
        getUserKey(contributorName, new UserKeyCallback() {
            @Override
            public void onUserKeyFound(String userKey) {
                if (userKey != null) {
                    tripDatabaseReference.child(CurrentTrip.getInstance().getCurrentTripId())
                            .child("users")
                            .child(userKey).setValue(contributorName);

                    userDatabaseReference.child(userKey).child("trips")
                            .child(CurrentTrip.getInstance().getCurrentTripName())
                            .setValue(CurrentTrip.getInstance().getCurrentTripId());
                }
            }
        });
    }

    private void getUserKey(String contributorName, final UserKeyCallback callback) {
        Query query = userDatabaseReference.orderByChild("username").equalTo(contributorName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String userKey = childSnapshot.getKey();
                        callback.onUserKeyFound(userKey);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onSubmitNote(String note) {
        String key = UUID.randomUUID().toString();
        tripDatabaseReference.child(CurrentTrip.getInstance().getCurrentTripId())
                .child("notes")
                .child(key).setValue(note);
    }

    private void loadContributors() {
        tripDatabaseReference.child(CurrentTrip.getInstance().getCurrentTripId()).child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot userSnapshot) {
                        if (userSnapshot.exists()) {
                            ArrayList<User> tempContributors
                                    = new ArrayList<>();
                            int totalContributors
                                    = (int) userSnapshot.getChildrenCount();
                            if (totalContributors == 0) {
                                contributorList.clear();
                            }
                            int[] loadedCount = {0};
                            for (DataSnapshot dataSnapshot
                                    : userSnapshot.getChildren()) {
                                String contributorId
                                        = dataSnapshot.getKey().toString();
                                loadContributorById(contributorId,
                                        tempContributors, totalContributors,
                                        loadedCount);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplication(),
                                "Failed to load destination",
                                Toast.LENGTH_SHORT).show();
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

    private void loadNotes() {
        tripDatabaseReference.child(CurrentTrip.getInstance().getCurrentTripId())
                .child("notes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot notesSnapshot) {
                        if (notesSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot
                                    : notesSnapshot.getChildren()) {
                                String noteValue
                                        = Objects.requireNonNull(
                                                dataSnapshot.getValue())
                                        .toString();
                                noteList.add(noteValue);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplication(),
                                "Failed to load destination",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public interface UserKeyCallback {
        void onUserKeyFound(String userKey);
    }
}