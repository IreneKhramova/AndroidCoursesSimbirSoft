package com.example.irene.androidcourses;

import java.sql.Timestamp;

public class Coordinates {
    private String user;
    private Timestamp timestamp;
    private double latitude;
    private double longitude;

    public Coordinates() {}

    public Coordinates(double latitude, double longitude, Timestamp timestamp, String user) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.user = user;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getUser() {
        return user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
