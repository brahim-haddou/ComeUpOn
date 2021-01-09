package com.example.comeupon.Models;

import java.io.Serializable;

public class Profile implements Serializable {
    private int id;
    private PlaceApp placeApp;
    private User user;
    private String image;
    private String phone;
    private String birthday;

    public Profile() {}
    public Profile(int id, PlaceApp placeApp, User user, String image, String phone, String birthday) {
        this.id = id;
        this.placeApp = placeApp;
        this.user = user;
        this.image = image;
        this.phone = phone;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PlaceApp getPlaceApp() {
        return placeApp;
    }

    public void setPlaceApp(PlaceApp placeApp) {
        this.placeApp = placeApp;
    }
}
