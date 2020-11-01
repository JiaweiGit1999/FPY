package com.example.fpy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class AnnouncementList implements Parcelable {
    private String title;
    private String description;
    private String imageurl;
    private Date date;

    public AnnouncementList() {
    }

    public AnnouncementList(String title, String description, String imageurl, Date date) {
        this.title = title;
        this.description = description;
        this.imageurl = imageurl;
        this.date = date;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected AnnouncementList(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageurl = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<AnnouncementList> CREATOR = new Creator<AnnouncementList>() {
        @Override
        public AnnouncementList createFromParcel(Parcel in) {
            return new AnnouncementList(in);
        }

        @Override
        public AnnouncementList[] newArray(int size) {
            return new AnnouncementList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(imageurl);
        parcel.writeLong(date.getTime());
    }
}
