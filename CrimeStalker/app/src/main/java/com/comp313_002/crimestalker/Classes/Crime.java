package com.comp313_002.crimestalker.Classes;

import java.util.ArrayList;
import java.util.List;
// class that has the table structure from Firebase database
public class Crime {
    private String userId;
    private String title;
    private String description;
    private Type type;
    private ArrayList<String> comments;
    private float latitude;
    private float longitude;
    private ArrayList<String> witnesses;
    private boolean policeReported;
    private String timeStamp;
    private String photoUrl;
    private boolean twitterShared;
    private String key;
    private String address;

    //Types of Crimes
    public enum Type {
        ASSAULT{
            @Override
            public String toString() {
                return "Assault";
            }
        },
        BREAK_AND_ENTER {
            @Override
            public String toString() {
                return "Break and Enter";
            }
        },
        ROBBERY{
            @Override
            public String toString() {
                return "Robbery";
            }
        },
        AUTO_THEFT {
            @Override
            public String toString() {
                return "Auto Theft";
            }
        },
        THEFT_OVER {
            @Override
            public String toString() {
                return "Theft Over";
            }
        }
    }
    // Default constructor
    public Crime() {
        comments = new ArrayList<>();
        witnesses =new ArrayList<>();
    }

    // Constructor
    public Crime(String title, String description) {
        this.title = title;
        this.description = description;
        witnesses =new ArrayList<>();
        comments = new ArrayList<>();
    }
    /*
     * Method to add comments to ArrayList
     * @param comment String value to add to comments
     * */
    public void addComments(String comment)
    {
        comments.add(comment);
    }
    /*
     * Method to add witness/userId to ArrayList to subscribe as a witness
     * @param userID Id of user to add to be added as a witness
     * */
    public void addWitness(String userID)
    {
        witnesses.add(userID);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getComments()
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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

    public ArrayList<String> getWitnesses() {
        return witnesses;
    }
    public int getNumberOfWitnesses() {
        return witnesses.size();
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

    public String getKey() {
        return key;
    }

    public void setKey(String keyfirebase) {
        this.key = keyfirebase;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
