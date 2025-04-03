package com.example.try2;

public class Product {
    private String name;
    private String price;
    private int imageResId;  // Image resource ID from drawable

    // Constructor
    public Product(String name, String price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}
