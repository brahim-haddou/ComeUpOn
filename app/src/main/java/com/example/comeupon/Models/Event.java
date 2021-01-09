package com.example.comeupon.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Event implements Serializable {
    private int id;
    private PlaceApp place_App_event;
    private Activity[] activityEvent;
    private String title;
    private String image;
    private String description;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Profile owner;


    public Event(int id, String title, String image, String description, LocalDateTime start_date,
                 LocalDateTime end_date, Profile owner, PlaceApp place_App_event, Activity[] activityEvent) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.owner = owner;
        this.place_App_event = place_App_event;
        this.activityEvent = activityEvent;
    }

    @Override
    public String toString() {
        return "\nEvent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", owner=" + owner +
                ", place_event=" + place_App_event +
                ", activityEvent=" + Arrays.toString(activityEvent) +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public PlaceApp getPlace_App_event() {
        return place_App_event;
    }

    public void setPlace_App_event(PlaceApp place_App_event) {
        this.place_App_event = place_App_event;
    }

    public Activity[] getActivityEvent() {
        return activityEvent;
    }

    public void setActivityEvent(Activity[] activityEvent) {
        this.activityEvent = activityEvent;
    }
}
