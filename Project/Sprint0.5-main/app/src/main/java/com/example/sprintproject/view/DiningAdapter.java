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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DiningAdapter extends RecyclerView.Adapter<DiningAdapter.DiningViewHolder> {

    private List<Dining> diningList;
    private DatabaseReference databaseReference;

    public DiningAdapter(List<Dining> diningList) {
        this.diningList = diningList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("dining");
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
            String id = dining.getId();
            if (id != null) {
                databaseReference.child(id).removeValue();
                diningList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, diningList.size());
            }
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
