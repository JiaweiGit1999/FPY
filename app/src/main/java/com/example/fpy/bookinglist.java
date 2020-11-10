package com.example.fpy;

public class bookinglist {
    public String date,duration,facility,status,time;

    private  bookinglist()
    {

    }
    public bookinglist(String date, String duration, String facility, String status, String time) {
        this.date = date;
        this.duration = duration;
        this.facility = facility;
        this.status = status;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
