package com.example.sprintproject.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelPost;

import java.util.List;

public class TravelPostAdapter extends RecyclerView.Adapter<TravelPostAdapter.TravelPostViewHolder> {

    private List<TravelPost> travelPostList;

    public TravelPostAdapter(List<TravelPost> travelPostList) {
        this.travelPostList = travelPostList;
    }

    @NonNull
    @Override
    public TravelPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel_post, parent, false);
        return new TravelPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelPostViewHolder holder, int position) {
        TravelPost travelPost = travelPostList.get(position);
        holder.usernameTextView.setText(travelPost.getUsername());
        holder.destinationTextView.setText(travelPost.getDestination());
        holder.durationTextView.setText(travelPost.getStartDate() + " -- " + travelPost.getEndDate());
        holder.accommodationTextView.setText(travelPost.getAccommodation());
        holder.transportationTextView.setText(travelPost.getTransportation());
        holder.diningTextView.setText(travelPost.getDining());
        holder.notesTextView.setText(travelPost.getNotes());

        holder.detailsButton.setOnClickListener(v -> {
            if (holder.detailsLayout.getVisibility() == View.GONE) {
                holder.detailsLayout.setVisibility(View.VISIBLE);
            } else {
                holder.detailsLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return travelPostList.size();
    }

    public static class TravelPostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView destinationTextView;
        TextView durationTextView;
        TextView accommodationTextView;
        TextView transportationTextView;
        TextView diningTextView;
        TextView notesTextView;
        Button detailsButton;
        LinearLayout detailsLayout;

        public TravelPostViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.text_view_username);
            destinationTextView = itemView.findViewById(R.id.text_view_destination);
            durationTextView = itemView.findViewById(R.id.text_view_duration);
            accommodationTextView = itemView.findViewById(R.id.text_view_accommodation);
            transportationTextView = itemView.findViewById(R.id.text_view_transportation);
            diningTextView = itemView.findViewById(R.id.text_view_dining);
            notesTextView = itemView.findViewById(R.id.text_view_notes);
            detailsButton = itemView.findViewById(R.id.button_details);
            detailsLayout = itemView.findViewById(R.id.layout_details);
        }
    }
}