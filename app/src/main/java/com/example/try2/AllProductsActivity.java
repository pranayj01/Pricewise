// File: AllProductsActivity.java
package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        recyclerView = findViewById(R.id.recycler_all_products);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        String json = getIntent().getStringExtra("all_products");
        Type listType = new TypeToken<List<Product>>() {}.getType();
        productList = new Gson().fromJson(json, listType);

        adapter = new ProductAdapter(this, productList, product -> {
            Intent intent = new Intent(AllProductsActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}
