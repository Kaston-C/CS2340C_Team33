package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.sprintproject.model.DestinationModel;

import java.util.Calendar;

public class DestinationViewModel extends AndroidViewModel {
    public ObservableField<String> location = new ObservableField<>("");
    public ObservableField<String> startDate = new ObservableField<>("");
    public ObservableField<String> endDate = new ObservableField<>("");
    public ObservableField<String> duration = new ObservableField<>("");
    public ObservableBoolean showInputs = new ObservableBoolean(false);

    private final DestinationModel model;

    public interface DatePickerListener {
        void onStartDateClick();
        void onEndDateClick();
    }

    private DatePickerListener datePickerListener;

    public DestinationViewModel(Application application) {
        super(application);
        model = new DestinationModel();
    }

    public void setDatePickerListener(DatePickerListener listener) {
        this.datePickerListener = listener;
    }

    public void onLogTravelClicked(View view) {
        showInputs.set(true);
    }

    public void onStartDateClicked(View view) {
        if (datePickerListener != null) {
            datePickerListener.onStartDateClick();
        }
    }

    public void onEndDateClicked(View view) {
        if (datePickerListener != null) {
            datePickerListener.onEndDateClick();
        }
    }

    public void onCalculateDuration(View view) {
        if (!startDate.get().isEmpty() && !endDate.get().isEmpty()) {
            model.setStartDate(parseDate(startDate.get()));
            model.setEndDate(parseDate(endDate.get()));
            int calculatedDuration = model.calculateDurationInDays();
            duration.set(String.valueOf(calculatedDuration));
            Toast.makeText(getApplication(), "Trip Duration: " + calculatedDuration + " days", Toast.LENGTH_SHORT).show();
        } else if (!startDate.get().isEmpty() && !duration.get().isEmpty()) {
            model.setStartDate(parseDate(startDate.get()));
            model.setDuration(Integer.parseInt(duration.get()));
            model.setEndDate(calculateEndDate(model.getStartDate(), model.getDuration()));
            endDate.set(formatDate(model.getEndDate()));
            Toast.makeText(getApplication(), "Calculated End Date: " + endDate.get(), Toast.LENGTH_SHORT).show();
        } else if (!endDate.get().isEmpty() && !duration.get().isEmpty()) {
            model.setEndDate(parseDate(endDate.get()));
            model.setDuration(Integer.parseInt(duration.get()));
            model.setStartDate(calculateStartDate(model.getEndDate(), model.getDuration()));
            startDate.set(formatDate(model.getStartDate()));
            Toast.makeText(getApplication(), "Calculated Start Date: " + startDate.get(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "You must provide at least two inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private Calendar parseDate(String date) {
        String[] parts = date.split("/");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[0]) - 1, Integer.parseInt(parts[1]));
        return calendar;
    }

    private Calendar calculateEndDate(Calendar start, int duration) {
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.DAY_OF_YEAR, duration);
        return end;
    }

    private Calendar calculateStartDate(Calendar end, int duration) {
        Calendar start = (Calendar) end.clone();
        start.add(Calendar.DAY_OF_YEAR, -duration);
        return start;
    }

    private String formatDate(Calendar date) {
        return (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR);
    }
}
