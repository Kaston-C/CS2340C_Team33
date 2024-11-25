package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Community;
import com.example.sprintproject.model.CommunityObserver;
import com.example.sprintproject.model.TravelPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTravelPosts;
    private TravelPostAdapter travelPostAdapter;
    private List<TravelPost> travelPostList;
    private DatabaseReference databaseReference;

    private List<CommunityObserver> observers = new ArrayList<>();

    public void registerObserver(CommunityObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CommunityObserver observer) {
        observers.remove(observer);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        CommunityObserver obs1 = new CommunityObserver();
        registerObserver(obs1);

        recyclerViewTravelPosts = findViewById(R.id.recycler_view_travel_posts);
        recyclerViewTravelPosts.setLayoutManager(new LinearLayoutManager(this));
        travelPostList = new ArrayList<>();
        travelPostAdapter = new TravelPostAdapter(travelPostList);
        recyclerViewTravelPosts.setAdapter(travelPostAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("communityTravelPosts");
        notifyObservers();

        ImageButton logisticsButton = findViewById(R.id.logistics_button);
        logisticsButton.setOnClickListener(
                v -> startActivity(new Intent(CommunityActivity.this, LogisticsActivity.class)));

        ImageButton destinationButton = findViewById(R.id.destination_button);
        destinationButton.setOnClickListener(
                v -> startActivity(new Intent(CommunityActivity.this, DestinationActivity.class)));

        ImageButton diningButton = findViewById(R.id.dining_button);
        diningButton.setOnClickListener(
                v -> startActivity(new Intent(CommunityActivity.this, DiningActivity.class)));

        ImageButton accommodationButton = findViewById(R.id.accommodation_button);
        accommodationButton.setOnClickListener(
                v -> startActivity(
                        new Intent(CommunityActivity.this, AccommodationActivity.class)));

        ImageButton communityButton = findViewById(R.id.community_button);
        communityButton.setSelected(true);

        ImageButton transportButton = findViewById(R.id.transportation_button);
        transportButton.setOnClickListener(
                v -> startActivity(new Intent(CommunityActivity.this, Transportation.class)));

        Button addPostButton = findViewById(R.id.add_post_button);
        addPostButton.setOnClickListener(
                v -> {
                Intent intent = new Intent(CommunityActivity.this, PostActivity.class);
                startActivity(intent);
                });
    }

    private void notifyObservers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (CommunityObserver observer : observers) {
                    observer.update(travelPostList, snapshot);
                }
                travelPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommunityActivity.this, "Failed to load travel posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}