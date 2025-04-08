// File: HomeActivity.java
package com.example.try2;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private final List<Product> productList = new ArrayList<>();
    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_hot_deals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        loadSingleProduct(); // Use the existing getProduct() method
    }

    private void loadSingleProduct() {
        ProductApi api = ApiClient.getRetrofitInstance().create(ProductApi.class);
        Call<Product> call = api.getProduct(); // ✅ Call the existing method

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.add(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to fetch product.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed();
            return;
        }

        this.doubleBackPressed = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
    }
}
