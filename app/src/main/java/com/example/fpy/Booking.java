package com.example.fpy;

import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {
    private String facility;
    private String date;
    private String time;
    private String duration;

    public Booking(String facility, String date, String time, String duration) {
        this.facility = facility;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public Booking() {
        this.facility = "";
        this.date = "";
        this.time = "";
        this.duration = "";
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(facility);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(duration);
    }

    public static final Parcelable.Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel parcel) {
            return new Booking(parcel);
        }

        @Override
        public Booking[] newArray(int i) {
            return new Booking[i];
        }
    };

    private Booking(Parcel in) {
        facility = in.readString();
        date = in.readString();
        time = in.readString();
        duration = in.readString();
    }
}
