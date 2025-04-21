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
    // Add this field
    private String platform;

    // Add getter and setter
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
    @SerializedName("price_history")
    private List<PriceHistory> priceHistory;

    public List<PriceHistory> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceHistory> priceHistory) {
        this.priceHistory = priceHistory;
    }

    @SerializedName("id")
    private String id;  // Add this field

    // ... your existing fields ...

    // Add getter for ID
    public String getId() {
        return id;
    }

    // Add setter for ID if needed
    public void setId(String id) {
        this.id = id;
    }
    @SerializedName("product_url")
    private String productUrl;


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
    @SerializedName("category")
    private String category;

    @SerializedName("brand")
    private String brand;


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

    public String getProductUrl() {
        return productUrl;
    }


    public String getTitle() { return name; }
    public List<String> getProperties() { return properties; }
    public String getPrice() { return price; }
    public String getMrp() { return mrp; }
    public String getDiscount() { return discount; }
    public boolean isLowest() { return isLowest; }
    public String getCategory() {
        return category;
    }
    public String getBrand() {
        return brand;
    }


    // --- Optional Setter ---
    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
}
