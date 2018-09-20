package com.example.t410.firebaseapp;

class Crime {

    private String title;
    private String description;
    private int urgency;

    // Default constructor
    public Crime() {

    }

    // Constructor
    public Crime(String title, String description, int urgency) {
        this.title = title;
        this.description = description;
        this.urgency = urgency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
