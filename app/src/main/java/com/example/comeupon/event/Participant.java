package com.example.comeupon.event;

import android.graphics.Bitmap;

public class Participant {
    public String username;
    public Bitmap image;

    public Participant(String name, Bitmap image) {
        this.username = name;
        this.image = image;
    }
}
