package com.example.sprintproject.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.CurrentTrip;
import com.example.sprintproject.model.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private Context context;
    private List<Trip> tripList;
    private DatabaseReference tripDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth firebaseAuth;

    public TripAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
        tripDatabaseReference = FirebaseDatabase.getInstance().getReference("trips");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void updateList(List<Trip> newList) {
        this.tripList.clear();
        this.tripList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.WHITE);
        holder.tvName.setTextColor(Color.BLACK);

        Trip trip = tripList.get(position);
        holder.tvName.setText(trip.getName());

        holder.btnDelete.setOnClickListener(v -> {
            getTripId(trip.getName(), tripId -> deleteTrip(tripId));
        });

        holder.itemView.setOnClickListener(v -> {
            getTripId(trip.getName(), tripId -> CurrentTrip.getInstance().setCurrentTripId(tripId));
            CurrentTrip.getInstance().setCurrentTripName(trip.getName());
            Toast.makeText(context, "Current Trip Set: " + trip.getName(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, LogisticsActivity.class);
            context.startActivity(intent);
        });
    }

    private void deleteTrip(String tripID) {
        String userId = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference userTripRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("trips")
                .child(tripID);

        DatabaseReference tripRef = FirebaseDatabase.getInstance()
                .getReference("trips")
                .child(tripID);

        userTripRef.removeValue();
        tripRef.removeValue();
    }

    private void getTripId(String tripName, final TripIdCallback callback) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        userDatabaseReference
                .child(userId)
                .child("trips")
                .child(tripName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String tripID = dataSnapshot.getValue(String.class);
                            callback.onCallback(tripID);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public interface TripIdCallback {
        void onCallback(String tripId);
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private Button btnDelete;

        public TripViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
