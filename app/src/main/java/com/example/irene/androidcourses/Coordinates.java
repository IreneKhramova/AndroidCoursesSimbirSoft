package com.example.irene.androidcourses;

public class Coordinates {
    private String user;
    private long timestamp;
    private double latitude;
    private double longitude;

    public Coordinates() {}

    public Coordinates(double latitude, double longitude, long timestamp, String user) {
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

    public void setTimestamp(long timestamp) {
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

    public long getTimestamp() {
        return timestamp;
    }
}
