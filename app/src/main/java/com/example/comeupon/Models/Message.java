package com.example.comeupon.Models;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private String text;
    private LocalDateTime time;
    private int event_Message_id;
    private int user_Message_id;

    public Message(int id, String text, LocalDateTime time, int event_Message_id, int user_Message_id) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.event_Message_id = event_Message_id;
        this.user_Message_id = user_Message_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getEvent_Message_id() {
        return event_Message_id;
    }

    public void setEvent_Message_id(int event_Message_id) {
        this.event_Message_id = event_Message_id;
    }

    public int getUser_Message_id() {
        return user_Message_id;
    }

    public void setUser_Message_id(int user_Message_id) {
        this.user_Message_id = user_Message_id;
    }
}
