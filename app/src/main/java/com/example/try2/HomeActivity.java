package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private final List<Product> fullProductList = new ArrayList<>();
    private final List<Product> displayList = new ArrayList<>();
    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        optimizeLayoutImages();

        recyclerView = findViewById(R.id.recycler_hot_deals);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new ProductAdapter(this, displayList);
        recyclerView.setAdapter(adapter);

        TextView viewMore = findViewById(R.id.view_more);
        viewMore.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
            intent.putExtra("all_products", new Gson().toJson(fullProductList));
            startActivity(intent);
        });

        loadProduct();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.nav_home){
//                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                return true;
            }
            else if(itemId == R.id.nav_search){
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                finish();
                return true;
            }
            else if(itemId == R.id.nav_profile){
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
        // Optional: Set default selected item
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void optimizeLayoutImages() {
        try {
            ImageView logoImage = findViewById(R.id.logo);
            ImageView mainBgImage = findViewById(R.id.main_bg_image);

            if (logoImage != null) {
                BitmapUtils.loadDrawableIntoImageView(this, logoImage, R.drawable.bg);
            }

            if (mainBgImage != null) {
                BitmapUtils.loadDrawableIntoImageView(this, mainBgImage, R.drawable.mainbg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // In HomeActivity.java, replace the loadProduct() method with this:

    private void loadProduct() {
        ProductApi api = ApiClient.getRetrofitInstance().create(ProductApi.class);

        // Load Flipkart products
        Call<List<Product>> flipkartCall = api.getFlipkartProducts();
        flipkartCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> flipkartProducts = response.body();
                    for (Product product : flipkartProducts) {
                        product.setPlatform("Flipkart");
                    }
                    fullProductList.addAll(flipkartProducts);

                    // After loading Flipkart, load Amazon
                    loadAmazonProducts();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Flipkart API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                // Try loading Amazon even if Flipkart fails
                loadAmazonProducts();
            }
        });
    }

    private void loadAmazonProducts() {
        ProductApi api = ApiClient.getRetrofitInstance().create(ProductApi.class);
        Call<List<Product>> amazonCall = api.getAmazonProducts();

        amazonCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> amazonProducts = response.body();
                    for (Product product : amazonProducts) {
                        product.setPlatform("Amazon");
                    }
                    fullProductList.addAll(amazonProducts);

                    // Shuffle and pick 5 random items for homepage
                    displayList.clear();
                    Collections.shuffle(fullProductList);
                    displayList.addAll(fullProductList.subList(0, Math.min(5, fullProductList.size())));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Amazon API error: " + t.getMessage(), Toast.LENGTH_LONG).show();

                // Still show products if we have Flipkart products
                if (!fullProductList.isEmpty()) {
                    displayList.clear();
                    Collections.shuffle(fullProductList);
                    displayList.addAll(fullProductList.subList(0, Math.min(5, fullProductList.size())));
                    adapter.notifyDataSetChanged();
                }
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
