package com.comp313_002.crimestalker.Classes;

import java.util.ArrayList;
import java.util.List;

public class Crime {
    private String userId;
    private String title;
    private String description;
    private String type;
    private List<String> comments = new ArrayList<>();
    private float latitude;
    private float longitude;
    private int witnesses;
    private boolean policeReported;
    private String timeStamp;
    private String photoUrl;
    private boolean twitterShared;

    // Default constructor
    public Crime() {

    }

    // Constructor
    public Crime(String title, String description, int urgency) {
        this.title = title;
        this.description = description;
    }
    public void addComments(String comment)
    {
        comments.add(comment);
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<String> getComments()
    {
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getWitnesses() {
        return witnesses;
    }

    public void setWitnesses(int witnesses) {
        this.witnesses = witnesses;
    }

    public boolean isPoliceReported() {
        return policeReported;
    }

    public void setPoliceReported(boolean policeReported) {
        this.policeReported = policeReported;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isTwitterShared() {
        return twitterShared;
    }

    public void setTwitterShared(boolean twitterShared) {
        this.twitterShared = twitterShared;
    }

}
