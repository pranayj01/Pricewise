package com.example.try2;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class PriceHistory {
    @SerializedName("date")
    private Date date;

    @SerializedName("price")
    private float price;

    public PriceHistory(Date date, float price) {
        this.date = date;
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public float getPrice() {
        return price;
    }
}