package com.example.fpy;

import java.sql.Time;

public class payment_model {

    private long amount;
    private String Status;
    private String user_id;
    private String description;
    private long time;

    private payment_model() {
    }

    public payment_model(long amount, String status, String user_id, String description, long time) {
        this.amount = amount;
        Status = status;
        this.user_id = user_id;
        this.description = description;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
