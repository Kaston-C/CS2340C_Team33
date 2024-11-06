package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LogisticsViewModel extends AndroidViewModel {
    private MutableLiveData<String> userKeyLiveData = new MutableLiveData<>();
    private ObservableField<String> note = new ObservableField<>("");
    private ObservableArrayList<User> contributorList = new ObservableArrayList<>();

    public ObservableField<String> getNote() { return note; }
    public ObservableArrayList<User> getContributorList() {
        return contributorList;
    }
    public LiveData<String> getUserKeyLiveData() { return userKeyLiveData; }

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

    public void onSubmitContributor(String contributorName) {
        getUserKey(contributorName, new UserKeyCallback() {
            @Override
            public void onUserKeyFound(String userKey) {
                if (userKey != null) {
                    String userId = mAuth.getCurrentUser().getUid();
                    userDatabaseReference.child(userId).child("trip").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String tripId = dataSnapshot.getValue(String.class);
                                tripDatabaseReference.child(tripId).child("users").child(userKey).setValue(contributorName);

                                userDatabaseReference.child(userKey).child("trip").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String oldTripId = snapshot.getValue(String.class);
                                        tripDatabaseReference.child(oldTripId).removeValue();
                                        userDatabaseReference.child(userKey).child("trip").setValue(tripId);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
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

    public interface UserKeyCallback {
        void onUserKeyFound(String userKey);
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
                                    String contributorId = dataSnapshot.getKey().toString();
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