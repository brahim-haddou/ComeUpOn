package com.example.comeupon.Models;

import java.io.Serializable;

public class PlaceApp implements Serializable {

    private int id;
    private String address;
    private String city;
    private String country;
    private double lat;
    private double lan;

    public PlaceApp(int id, String address, String city, String country, double lat, double lan) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.country = country;
        this.lat = lat;
        this.lan = lan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }
}

