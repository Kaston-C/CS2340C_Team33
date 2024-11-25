package com.example.sprintproject.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Dining;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DiningAdapter extends RecyclerView.Adapter<DiningAdapter.DiningViewHolder> {

    private List<Dining> diningList;
    private DatabaseReference diningDatabaseReference;
    private DatabaseReference tripDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth firebaseAuth;

    public DiningAdapter(List<Dining> diningList) {
        this.diningList = diningList;
        diningDatabaseReference = FirebaseDatabase.getInstance().getReference("dining");
        tripDatabaseReference = FirebaseDatabase.getInstance().getReference("trips");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void updateList(List<Dining> newList) {
        this.diningList.clear();
        this.diningList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public DiningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dining, parent, false);
        return new DiningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiningViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.WHITE);
        holder.tvDate.setTextColor(Color.BLACK);
        holder.tvTime.setTextColor(Color.BLACK);

        Dining dining = diningList.get(position);
        holder.tvDate.setText(dining.getDate());
        holder.tvTime.setText(dining.getTime());
        holder.tvLocation.setText(dining.getLocation());
        holder.tvWebsite.setText(dining.getWebsite());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        LocalDateTime reservationDateTime = LocalDateTime.parse(dining.getDate()
                + " " + dining.getTime(), formatter);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (reservationDateTime.isBefore(currentDateTime)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            holder.tvDate.setTextColor(Color.RED);
            holder.tvTime.setTextColor(Color.RED);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.tvDate.setTextColor(Color.BLACK);
            holder.tvTime.setTextColor(Color.BLACK);
        }

        holder.btnDelete.setOnClickListener(v -> {
            String location = dining.getLocation();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            userDatabaseReference.child(currentUser.getUid())
                    .child("trip")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String tripId = dataSnapshot.getValue(String.class);
                                if (tripId != null) {
                                    tripDatabaseReference.child(tripId)
                                            .child("dining")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        for (DataSnapshot diningEntry : snapshot.getChildren()) {
                                                            String diningLocation = diningEntry.getKey();
                                                            String diningKey = diningEntry.getValue(String.class);
                                                            if (location.equals(diningLocation)) {
                                                                tripDatabaseReference.child(tripId)
                                                                        .child("dining").child(diningEntry.getKey())
                                                                        .removeValue()
                                                                        .addOnCompleteListener(task -> {
                                                                            if (task.isSuccessful()) {
                                                                                diningDatabaseReference.child(diningKey)
                                                                                        .removeValue()
                                                                                        .addOnCompleteListener(deleteTask -> {
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

        });
    }

    @Override
    public int getItemCount() {
        return diningList.size();
    }

    public static class DiningViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvTime;
        private TextView tvLocation;
        private TextView tvWebsite;
        private Button btnDelete;

        public DiningViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvWebsite = itemView.findViewById(R.id.tvWebsite);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
