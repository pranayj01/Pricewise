package com.example.try2;

import java.io.Serializable;

public class Product implements Serializable {
    private int imageResId;
    private String name;
    private String price;

    public Product(int imageResId, String name, String price) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
    }

    public int getImageResId() { return imageResId; }
    public String getName() { return name; }
    public String getPrice() { return price; }
}
