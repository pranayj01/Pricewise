package com.example.try2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("b1eac257-db48-4c5d-bcb6-426491872bcb")
    Call<Product> getProduct();

}
