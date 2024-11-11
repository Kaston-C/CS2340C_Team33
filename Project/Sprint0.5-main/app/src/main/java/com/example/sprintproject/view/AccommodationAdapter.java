package com.example.sprintproject.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter
        .AccommodationViewHolder> {
    private List<Accommodation> accommodationList;

    public AccommodationAdapter(List<Accommodation> accommodationList) {
        this.accommodationList = accommodationList;
    }

    public void updateList(List<Accommodation> newList) {
        this.accommodationList.clear();
        this.accommodationList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public AccommodationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_accommodation, parent, false);
        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccommodationViewHolder holder, int position) {
        holder.datesText.setTextColor(Color.BLACK);

        Accommodation accommodation = accommodationList.get(position);
        holder.nameText.setText(accommodation.getName());
        holder.locationText.setText(accommodation.getLocation());
        holder.datesText.setText("Check-in: " + accommodation.getCheckInDate()
                + " - Check-out: " + accommodation.getCheckOutDate());
        holder.roomsText.setText("Rooms: " + accommodation.getNumberOfRooms());

        //Check if past reservation
        if (isPastReservation(accommodation.getCheckInDate())) {
            holder.datesText.setTextColor(Color.RED); //EXPIRED
        }
    }

    @Override
    public int getItemCount() {
        return accommodationList.size();
    }

    // Checks if in the past
    private boolean isPastReservation(String checkInDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date checkInDate = sdf.parse(checkInDateStr);
            return checkInDate != null && checkInDate.before(new Date());
        } catch (ParseException e) {
            return false;
        }
    }
    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView locationText;
        private TextView datesText;
        private TextView roomsText;

        public AccommodationViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.accommodation_name);
            locationText = itemView.findViewById(R.id.accommodation_location);
            datesText = itemView.findViewById(R.id.accommodation_dates);
            roomsText = itemView.findViewById(R.id.accommodation_rooms);
        }
    }

}


