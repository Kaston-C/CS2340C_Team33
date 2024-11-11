package com.example.sprintproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.Locale;

public class Accommodation {
    private String id;
    private String checkInDate;
    private String checkOutDate;
    private String location;
    private int numberOfRooms;
    private String roomType;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int roomNumber;

    public Accommodation() {
    }

    public Accommodation(String id, String name, String location,
                         LocalDateTime startDateTime, LocalDateTime endDateTime,
                         String roomType, int roomNumber) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
            return checkOutDate != null && checkOutDate.after(checkInDate) || checkOutDate
                    .equals(checkInDate);
        } catch (ParseException e) {
            return false;
        }
    }
}