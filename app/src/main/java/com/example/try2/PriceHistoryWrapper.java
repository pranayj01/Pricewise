package com.example.try2;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PriceHistoryWrapper {
    @SerializedName("productId")
    private String productId;

    @SerializedName("history")
    private List<PriceHistory> history;

    public String getProductId() {
        return productId;
    }

    public List<PriceHistory> getHistory() {
        return history;
    }
}
