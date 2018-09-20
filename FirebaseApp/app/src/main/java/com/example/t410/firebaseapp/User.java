package com.example.t410.firebaseapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private FirebaseAuth firebase;
    private DatabaseReference users;
    private String userId;
    private String address;
    private String phoneNumber;

    // Default constructor
    public User() {

    }

    public User(String userId, String email, String password, String address, String phoneNumber) {
        this.userId = userId;
        this.address = address;
        this.phoneNumber = phoneNumber;

        this.writeNewUser();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

   /* private void addNewUser(){
        firebase.createUserWithEmailAndPassword(email, password).catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            // ...
        });
    }*/

    private void writeNewUser() {
        users = FirebaseDatabase.getInstance().getReference();
        users.child("users").push().setValue(this);
    }
}
