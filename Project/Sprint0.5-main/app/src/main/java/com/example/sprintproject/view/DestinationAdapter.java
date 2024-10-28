package com.example.sprintproject.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Destination;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private ObservableArrayList<Destination> destinations;
    private OnDestinationClickListener listener;

    public interface OnDestinationClickListener {
        void onDestinationClick(Destination destination);
    }

    public DestinationAdapter(ObservableArrayList<Destination> destinations, OnDestinationClickListener listener) {
        this.destinations = destinations;
        this.listener = listener;
    }

    @Override
    public DestinationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DestinationViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.bind(destination);
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public class DestinationViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView;
        TextView datesTextView;
        TextView daysPlannedTextView;

        public DestinationViewHolder(View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.destination_location);
            datesTextView = itemView.findViewById(R.id.destination_dates);
            daysPlannedTextView = itemView.findViewById(R.id.destination_days_planned);
        }

        public void bind(Destination destination) {
            locationTextView.setText(destination.getLocation());
            datesTextView.setText(destination.getStartDate() + " - " + destination.getEndDate());

            int daysPlanned = destination.getDuration();
            daysPlannedTextView.setText(daysPlanned > 0 ? daysPlanned + " days planned" : "0 days planned");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDestinationClick(destination);
                }
            });
        }
    }
}
