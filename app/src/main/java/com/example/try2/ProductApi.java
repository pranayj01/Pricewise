package com.example.try2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("7c8770d0-349e-49a4-9ff4-df82b3db6520")
    Call<List<Product>> getFlipkartProducts();

    @GET("c932df2a-186b-4529-a6f7-aa128226acb3") // Amazon endpoint (replace with actual endpoint)
    Call<List<Product>> getAmazonProducts();

}
