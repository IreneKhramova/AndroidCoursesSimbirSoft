package com.example.irene.androidcourses;

public class User {
    private String phone;
    private String name;
    private String email;
    private double latitude;
    private double longitude;

    public User() { }

    public User(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public User(String name, String phone, String email, double latitude, double longitude) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
