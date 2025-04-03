package com.example.try2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_hot_deals);

        // Use LinearLayoutManager with HORIZONTAL orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        productList = new ArrayList<>();
        productList.add(new Product("Men Slim Jeans", "₹999", R.drawable.product1));
        productList.add(new Product("Loose Fit Grey Jeans", "₹1,499", R.drawable.product2));
        productList.add(new Product("Light Blue Jeans", "₹899", R.drawable.product3));
        productList.add(new Product("Tapered Fit Blue Jeans", "₹2,299", R.drawable.product4));
         // Add more items

        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
    }

}
