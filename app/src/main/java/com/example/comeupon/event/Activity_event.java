package com.example.comeupon.event;

import android.graphics.Bitmap;

public class Activity_event {


    public String name, category;
    public int number_activity, number_participant;
    public Bitmap image;

    public Activity_event(String name, String category, int number_activity, int number_participant, Bitmap image) {
        this.name = name;
        this.category = category;
        this.number_activity = number_activity;
        this.number_participant = number_participant;
        this.image = image;
    }

}
