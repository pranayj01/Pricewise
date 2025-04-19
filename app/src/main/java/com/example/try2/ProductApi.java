package com.example.try2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("b1e945ee-8eb2-4a22-8f87-899b66c0330b")
    Call<List<Product>> getFlipkartProducts();

    @GET("734be14f-e0e8-4845-b3e6-5dd6aba52881") // Amazon endpoint (replace with actual endpoint)
    Call<List<Product>> getAmazonProducts();

    @GET("dc587bba-1863-4549-8813-4406e054ed79") // your mocky ID
    Call<List<PriceHistoryWrapper>> getAllPriceHistories();
}
