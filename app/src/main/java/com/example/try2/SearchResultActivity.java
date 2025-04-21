package com.example.try2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Amazon"));
        tabLayout.addTab(tabLayout.newTab().setText("Flipkart"));
        tabLayout.addTab(tabLayout.newTab().setText("Myntra"));
        tabLayout.addTab(tabLayout.newTab().setText("Croma"));

        searchQuery = getIntent().getStringExtra("search_query");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            fetchSearchResults(searchQuery, "Amazon"); // Default to Amazon
        } else {
            Toast.makeText(this, "No search query provided.", Toast.LENGTH_SHORT).show();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String platform = tab.getText().toString();
                fetchSearchResults(searchQuery, platform);
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

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
//            case "myntra":
//                call = api.getMyntraProducts(); // Ensure you implement this
//                break;
//            case "croma":
//                call = api.getCromaProducts(); // Ensure you implement this
//                break;
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

                    for (Product product : allProducts) {
                        if ((product.getTitle() != null && product.getTitle().toLowerCase().contains(query.toLowerCase())) ||
                                (product.getBrand() != null && product.getBrand().toLowerCase().contains(query.toLowerCase())) ||
                                (product.getCategory() != null && product.getCategory().toLowerCase().contains(query.toLowerCase()))) {
                            filteredProducts.add(product);
                        }
                    }

                    adapter.updateList(filteredProducts);

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
