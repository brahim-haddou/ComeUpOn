package com.example.comeupon.eventHomeList;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class EventItem {

    public int ID_event;
    public String Title, Hour, Date;
    public ArrayList<Integer> images;
    public LatLng center;

    public EventItem(int id, String title, String hour, String date, ArrayList<Integer> images,Double lat, Double lng) {
        this.ID_event =id;
        this.Title = title;
        this.Hour = hour;
        this.Date = date;
        this.images = images;
        this.center = new LatLng(lat,lng);
    }
}
