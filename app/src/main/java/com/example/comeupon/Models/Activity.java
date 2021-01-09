package com.example.comeupon.Models;

import java.io.Serializable;

public class Activity implements Serializable {
    private int id;
    private String name;
    private String category;
    private String image;
    private int number_Activity;
    private int number_Participant;

    public Activity(int id, String name, String category, String image, int number_Activity, int number_Participant) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.image = image;
        this.number_Activity = number_Activity;
        this.number_Participant = number_Participant;
    }

    @Override
    public String toString() {
        return "\nActivity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", image='" + image + '\'' +
                ", number_Activity=" + number_Activity +
                ", number_Participant=" + number_Participant +
                "}\n";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNumber_Activity() {
        return number_Activity;
    }

    public void setNumber_Activity(int number_Activity) {
        this.number_Activity = number_Activity;
    }

    public int getNumber_Participant() {
        return number_Participant;
    }

    public void setNumber_Participant(int number_Participant) {
        this.number_Participant = number_Participant;
    }
}
