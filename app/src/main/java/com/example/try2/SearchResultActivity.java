package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    TabLayout tabLayout;
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.recycler_view_results);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        // Initialize the adapter with an empty list (this is just for setup)
        List<Product> displayList = new ArrayList<>();
        adapter = new ProductAdapter(this, displayList, product -> {
            // When a product is clicked, navigate to ProductDetailActivity
            Intent intent = new Intent(SearchResultActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Amazon"));
        tabLayout.addTab(tabLayout.newTab().setText("Flipkart"));
        tabLayout.addTab(tabLayout.newTab().setText("Myntra"));
        tabLayout.addTab(tabLayout.newTab().setText("Croma"));

        searchQuery = getIntent().getStringExtra("search_query");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            fetchSearchResults(searchQuery, "Amazon");  // Fetch results for Amazon initially
        } else {
            Toast.makeText(this, "No search query provided.", Toast.LENGTH_SHORT).show();
        }

        // Listen for tab selection to change platform
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String platform = tab.getText().toString();
                fetchSearchResults(searchQuery, platform);  // Fetch results based on selected platform
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // Fetch results based on search query and selected platform
    private void fetchSearchResults(String query, String platform) {
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ProductApi api = retrofit.create(ProductApi.class);

        Call<List<Product>> call;
        switch (platform.toLowerCase()) {
            case "amazon":
                call = api.getAmazonProducts();
                break;
            case "flipkart":
                call = api.getFlipkartProducts();
                break;
            default:
                Toast.makeText(this, "Unknown platform.", Toast.LENGTH_SHORT).show();
                return;
        }

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> allProducts = response.body();
                    List<Product> filteredProducts = new ArrayList<>();

                    // Filter products based on search query
                    for (Product product : allProducts) {
                        if ((product.getName() != null && product.getName().toLowerCase().contains(query.toLowerCase())) ||
                                (product.getBrand() != null && product.getBrand().toLowerCase().contains(query.toLowerCase())) ||
                                (product.getCategory() != null && product.getCategory().toLowerCase().contains(query.toLowerCase()))) {
                            filteredProducts.add(product);
                        }
                    }

                    // Update adapter with filtered products
                    adapter.updateList(filteredProducts);

                    // Show a message if no products are found
                    if (filteredProducts.isEmpty()) {
                        Toast.makeText(SearchResultActivity.this, "No results found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchResultActivity.this, "Failed to fetch results.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SearchResultActivity.this, "Error fetching results.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
