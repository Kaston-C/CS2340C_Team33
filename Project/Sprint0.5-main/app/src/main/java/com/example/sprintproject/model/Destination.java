package com.example.sprintproject.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Destination {
    private String id;
    private String location;
    private String startDate;
    private String endDate;
    private int duration;

    public Destination() {
        this("", "", "", "", 0);
    }

    public Destination(String id, String location, String startDate, String endDate, int duration) {
        this.id = id;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getDuration() {
        return duration;
    }

    public String getName() {
        return location;
    }

    public static int calculateDurationInDays(String startDateStr, String endDateStr) {
        Calendar startDate = parseDate(startDateStr);
        Calendar endDate = parseDate(endDateStr);
        if (startDate == null || endDate == null) {
            return -1;
        }
        int diffInMillis = (int) (endDate.getTimeInMillis() - startDate.getTimeInMillis());
        int diffInDays = (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
        return diffInDays;
    }

    public static String calculateEndDate(String startDateStr, int durationDays) {
        Calendar startDate = parseDate(startDateStr);
        if (startDate == null) {
            return null;
        }
        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DAY_OF_YEAR, durationDays - 1);
        return formatDate(endDate);
    }

    public static String calculateStartDate(String endDateStr, int durationDays) {
        Calendar endDate = parseDate(endDateStr);
        if (endDate == null) {
            return null;
        }
        Calendar startDate = (Calendar) endDate.clone();
        startDate.add(Calendar.DAY_OF_YEAR, -durationDays + 1);
        return formatDate(startDate);
    }

    private static Calendar parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = dateFormat.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            return null;
        }
    }

    private static String formatDate(Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(date.getTime());
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
