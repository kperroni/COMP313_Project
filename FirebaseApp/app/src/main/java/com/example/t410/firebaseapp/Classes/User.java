package com.example.t410.firebaseapp.Classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.t410.firebaseapp.Activities.MainActivity;
import com.example.t410.firebaseapp.Activities.RegisterUserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean allowPoliceCalls;

    // Default constructor
    public User() {

    }

    public User(String firstName, String lastName, String address, String phoneNumber, Boolean allowPoliceCalls) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.allowPoliceCalls = allowPoliceCalls;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAllowPoliceCalls() {
        return allowPoliceCalls;
    }

    public void setAllowPoliceCalls(boolean allowPoliceCalls) {
        this.allowPoliceCalls = allowPoliceCalls;
    }
}
