package com.example.smartbank;


public class PlaceModel {
    private String bank;
    private String ifsc;
    private String branch;
    private String address;
    private String city1;
    private String city2;
    private String state;
    private int stdcode;
    private String phone;
    private double longitude;
    private double latitude;
    private String location;
    private double dt;

    public PlaceModel() {
    }

    //deploy
    public PlaceModel(String bank,String ifsc,String branch,String address,String city1,String city2,String state,int stdcode,String phone,double longitude,double latitude,String location) {
        this.bank = bank;
        this.branch = branch;
        this.ifsc = ifsc;
        this.address = address;
        this.city1 = city1;
        this.city2 = city2;
        this.state = state;
        this.stdcode = stdcode;
        this.phone = phone;
        this.longitude=longitude;
        this.latitude=latitude;
        this.location = location;
    }

    public PlaceModel(String bank,String ifsc,String branch,String address,String city1,String city2,String state,int stdcode,String phone,double longitude,double latitude,String location,double dt) {

        this.bank = bank;
        this.branch = branch;
        this.ifsc = ifsc;
        this.address = address;
        this.city1 = city1;
        this.city2 = city2;
        this.state = state;
        this.stdcode = stdcode;
        this.phone = phone;
        this.longitude=longitude;
        this.latitude=latitude;
        this.location = location;
        this.dt = dt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStdcode() {
        return stdcode;
    }

    public void setStdcode(int stdcode) {
        this.stdcode = stdcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getDt() {
        return dt;
    }

    public void setDt(double dt) {
        this.dt = dt;
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
}
