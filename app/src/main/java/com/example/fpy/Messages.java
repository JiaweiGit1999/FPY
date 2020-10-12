package com.example.fpy;

import java.sql.Time;
import java.sql.Timestamp;
//message object
public class Messages {
    private String name;
    private String message;
    private Timestamp timeStamp;

    public Messages(String name, String message, Timestamp timeStamp) {
        this.name = name;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }
}
