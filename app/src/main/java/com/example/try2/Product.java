package com.example.try2;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    // For local drawable images
    private int imageResId;

    // For API-based image
    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("title")
    private String name;


    @SerializedName("properties")
    private List<String> properties;

    @SerializedName("price")
    private String price;

    @SerializedName("mrp")
    private String mrp;

    @SerializedName("discount")
    private String discount;

    @SerializedName("is_lowest")
    private boolean isLowest;

    // --- Constructors ---

    // Constructor for static/local use
    public Product(int imageResId, String name, String price) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
    }

    // --- Getters ---

    public int getImageResId() { return imageResId; }
    public String getImageUrl() { return imageUrl; }
    public String getName() { return name; }
    public List<String> getProperties() { return properties; }
    public String getPrice() { return price; }
    public String getMrp() { return mrp; }
    public String getDiscount() { return discount; }
    public boolean isLowest() { return isLowest; }

    // --- Optional Setter ---
    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
}
