package com.example.fpy;

import java.util.Date;

public class UserImage {
    private static UserImage instance;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public UserImage() {
        date = new Date();
    }

    public static UserImage getInstance() {
        if (instance == null)
            instance = new UserImage();
        return instance;
    }
}
