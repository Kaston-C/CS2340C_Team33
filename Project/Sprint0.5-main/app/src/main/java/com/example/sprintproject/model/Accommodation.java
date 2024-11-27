package com.example.sprintproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.Locale;

public class Accommodation {
    private String checkInDate;
    private String checkOutDate;
    private String location;
    private int numberOfRooms;
    private String roomType;
    private String name;

    public Accommodation() {
    }

    public Accommodation(String checkInDate, String checkOutDate,
                         String location, int numberOfRooms, String roomType, String name) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.location = location;
        this.numberOfRooms = numberOfRooms;
        this.roomType = roomType;
        this.name = name;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isCheckOutDateValid(String checkInDateStr, String checkOutDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date checkInDate = sdf.parse(checkInDateStr);
            Date checkOutDate = sdf.parse(checkOutDateStr);
            if (checkOutDate != null && checkInDate != null) {
                return checkOutDate.after(checkInDate) || checkOutDate
                        .equals(checkInDate);
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    public LocalDateTime getCheckInDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        return LocalDateTime.parse(checkInDate + " 12:00", dateFormatter);
    }

    public LocalDateTime getCheckOutDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        return LocalDateTime.parse(checkOutDate + " 12:00", dateFormatter);
    }
}