package com.example.fpy;

import android.app.Application;

import java.util.Calendar;
import java.util.List;

public class QrCodeList extends Application {
    private static QrCodeList instance;
    private String username, ic, contact;
    private Calendar expire;
    private List<String> unit;

    public List<String> getUnit() {
        return unit;
    }

    public void setUnit(List<String> unit) {
        this.unit = unit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Calendar getExpire() {
        return expire;
    }

    public void setExpire(Calendar expire) {
        this.expire = expire;
    }

    public QrCodeList() {

    }

    public static QrCodeList getInstance() {
        if (instance == null)
            instance = new QrCodeList();
        return instance;
    }
}
