package com.example.sprintproject.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.User;

import java.util.List;

public class LogisticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_USER = 0;
    private static final int TYPE_NOTE = 1;

    private OnLogisticsClickListener listener;
    private List<Object> itemList;

    public LogisticsAdapter(List<Object> items, OnLogisticsClickListener listener) {
        this.itemList = items;
        this.listener = listener;
    }

    public void updateList(List<Object> newList) {
        itemList.clear();
        itemList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof User) {
            return TYPE_USER;
        } else {
            return TYPE_NOTE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
            return new NoteViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_USER) {
            User user = (User) itemList.get(position);
            UserViewHolder userHolder = (UserViewHolder) holder;
            userHolder.username.setText(user.getUsername().substring(0, user.getUsername().indexOf('@')));
            if (user.getStartVacation() == null) {
                userHolder.vacation.setText("Vacation not set");

            } else {
                userHolder.vacation.setText("Vacation: " + user.getStartVacation() + "–" + user.getEndVacation());
            }
            userHolder.itemView.setOnClickListener(v -> listener.onLogisticsClick(user));
        } else {
            String note = (String) itemList.get(position);
            NoteViewHolder noteHolder = (NoteViewHolder) holder;
            noteHolder.noteText.setText(note);
            noteHolder.itemView.setOnClickListener(v -> listener.onLogisticsClick(note));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView username, vacation;

        UserViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            vacation = itemView.findViewById(R.id.vacation);
        }
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteText;

        NoteViewHolder(View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.noteText);
        }
    }

    public interface OnLogisticsClickListener {
        void onLogisticsClick(Object object);
    }
}