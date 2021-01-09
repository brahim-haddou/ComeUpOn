package com.example.comeupon.Models;

public class Follower  {

    private int id;
    private int user_id;
    private int[] followers;

    public Follower(int id, int user_id, int[] followers) {
        this.id = id;
        this.user_id = user_id;
        this.followers = followers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int[] getFollowers() {
        return followers;
    }

    public void setFollowers(int[] followers) {
        this.followers = followers;
    }
}
