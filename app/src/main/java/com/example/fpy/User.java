package com.example.fpy;

import android.app.Application;

import java.util.Vector;

public class User extends Application {
    private static User instance;

    private String username;
    private String ic;
    private String contact;
    private String email;
    private String gender;
    private String uid;

    public User() {
    }
    public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getIc() {
        return ic;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }
}
