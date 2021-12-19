package com.example.loginapi.loginapi.bank;

public class NearBody {

    private double longitude;
    private double latitude;
    private double distance;

    public NearBody() {
    }

    //deploy
    public NearBody(double longitude,double latitude,double distance) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
