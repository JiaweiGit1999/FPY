package com.example.fpy;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.ref.Reference;

public class unit_model implements Parcelable {
    private String name;
    private String landlord;
    private String ic;
    private String email;
    private String contact;


    private unit_model() {
    }

    public unit_model(String name, String landlord, String ic, String email, String contact) {
        this.name = name;
        this.landlord = landlord;
        this.ic = ic;
        this.email = email;
        this.contact = contact;
    }

    protected unit_model(Parcel in) {
        name = in.readString();
        landlord = in.readString();
        ic = in.readString();
        email = in.readString();
        contact = in.readString();
    }

    public static final Creator<unit_model> CREATOR = new Creator<unit_model>() {
        @Override
        public unit_model createFromParcel(Parcel in) {
            return new unit_model(in);
        }

        @Override
        public unit_model[] newArray(int size) {
            return new unit_model[size];
        }
    };

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandlord() {
        return landlord;
    }

    public void setLandlord(String landlord) {
        this.landlord = landlord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(landlord);
        parcel.writeString(ic);
        parcel.writeString(email);
        parcel.writeString(contact);
    }
}
