package com.example.try2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("4e71913b-6aeb-46da-af8e-8c454ce70092")
    Call<List<Product>> getProduct();
}
