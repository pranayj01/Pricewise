package com.example.try2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList = new ArrayList<>();
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);  // ✅ Fixed layout name

        recyclerView = findViewById(R.id.recycler_view_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        searchQuery = getIntent().getStringExtra("search_query");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            fetchSearchResults(searchQuery);
        } else {
            Toast.makeText(this, "No search query provided.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSearchResults(String query) {
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ProductApi api = retrofit.create(ProductApi.class);
        Call<List<Product>> call = api.getFlipkartProducts();  // Replace with appropriate API call

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> allProducts = response.body();
                    List<Product> filteredProducts = new ArrayList<>();

                    for (Product product : allProducts) {
                        if (product.getTitle() != null  && product.getTitle().toLowerCase().contains(query.toLowerCase()) ||
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
