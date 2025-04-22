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

    private RecyclerView electronicsRecyclerView;
    private ProductAdapter electronicsAdapter;
    private final List<Product> electronicsList = new ArrayList<>();

    private RecyclerView healthRecyclerView;
    private ProductAdapter healthAdapter;
    private final List<Product> healthList = new ArrayList<>();

    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        optimizeLayoutImages();

        // Hot Deals
        recyclerView = findViewById(R.id.recycler_hot_deals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ProductAdapter(this, displayList, product -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Electronics
        electronicsRecyclerView = findViewById(R.id.recycler_electronics);
        electronicsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        electronicsAdapter = new ProductAdapter(this, electronicsList, product -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
        electronicsRecyclerView.setAdapter(electronicsAdapter);

        // Health
        healthRecyclerView = findViewById(R.id.recycler_health);
        healthRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        healthAdapter = new ProductAdapter(this, healthList, product -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
        healthRecyclerView.setAdapter(healthAdapter);

        // View More Buttons
        TextView viewMore = findViewById(R.id.view_more);
        viewMore.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
            intent.putExtra("all_products", new Gson().toJson(fullProductList));
            startActivity(intent);
        });

        TextView viewMoreElectronics = findViewById(R.id.view_more_electronics);
        viewMoreElectronics.setOnClickListener(v -> {
            List<Product> electronicsOnly = new ArrayList<>();
            for (Product product : fullProductList) {
                if (product.getCategory() != null && product.getCategory().toLowerCase().contains("electronics")) {
                    electronicsOnly.add(product);
                }
            }
            Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
            intent.putExtra("all_products", new Gson().toJson(electronicsOnly));
            startActivity(intent);
        });

        TextView viewMoreHealth = findViewById(R.id.view_more_health);
        viewMoreHealth.setOnClickListener(v -> {
            List<Product> healthOnly = new ArrayList<>();
            for (Product product : fullProductList) {
                if (product.getCategory() != null && product.getCategory().toLowerCase().contains("health")) {
                    healthOnly.add(product);
                }
            }
            Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
            intent.putExtra("all_products", new Gson().toJson(healthOnly));
            startActivity(intent);
        });

        loadProduct();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });

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

    private void loadProduct() {
        ProductApi api = ApiClient.getRetrofitInstance().create(ProductApi.class);
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
                    loadAmazonProducts();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Flipkart API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

                    categorizeAndDisplay();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Amazon API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                categorizeAndDisplay(); // even if Amazon fails
            }
        });
    }

    private void categorizeAndDisplay() {
        // Categorize
        electronicsList.clear();
        healthList.clear();
        for (Product product : fullProductList) {
            if (product.getCategory() != null) {
                String category = product.getCategory().toLowerCase();
                if (category.contains("electronics")) {
                    electronicsList.add(product);
                } else if (category.contains("health") || category.contains("nutrition")) {
                    healthList.add(product);
                }
            }
        }

        // Shuffle and show 5 from each
        List<Product> shuffledElectronics = new ArrayList<>(electronicsList);
        Collections.shuffle(shuffledElectronics);
        electronicsList.clear();
        electronicsList.addAll(shuffledElectronics.subList(0, Math.min(5, shuffledElectronics.size())));
        electronicsAdapter.notifyDataSetChanged();

        List<Product> shuffledHealth = new ArrayList<>(healthList);
        Collections.shuffle(shuffledHealth);
        healthList.clear();
        healthList.addAll(shuffledHealth.subList(0, Math.min(5, shuffledHealth.size())));
        healthAdapter.notifyDataSetChanged();

        displayList.clear();
        Collections.shuffle(fullProductList);
        displayList.addAll(fullProductList.subList(0, Math.min(5, fullProductList.size())));
        adapter.notifyDataSetChanged();
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
