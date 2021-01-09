package com.example.comeupon.Models;

public class Participant {
    private int id;
    private boolean stat;
    private int event_participant_id;
    private int user_participant_id;

    public Participant(int id, boolean stat, int event_participant_id, int user_participant_id) {
        this.id = id;
        this.stat = stat;
        this.event_participant_id = event_participant_id;
        this.user_participant_id = user_participant_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStat() {
        return stat;
    }

    public void setStat(boolean stat) {
        this.stat = stat;
    }

    public int getEvent_participant_id() {
        return event_participant_id;
    }

    public void setEvent_participant_id(int event_participant_id) {
        this.event_participant_id = event_participant_id;
    }

    public int getUser_participant_id() {
        return user_participant_id;
    }

    public void setUser_participant_id(int user_participant_id) {
        this.user_participant_id = user_participant_id;
    }
}
