package com.example.fpy;

public class paymentlist {
    public String description;
    public double amount;

    public paymentlist(String description, double amount) {
        this.description = description;
        this.amount = amount;

    }
    private paymentlist()
    {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}

