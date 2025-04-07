package com.example.try2;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private boolean doubleBackPressed =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_hot_deals);
        adapter = new ProductAdapter(this, productList); // ✅ Will work now

        // Use LinearLayoutManager with HORIZONTAL orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        productList = new ArrayList<>();
        productList.add(new Product(R.drawable.product1, "Men Slim Jeans", "₹999"));
        productList.add(new Product(R.drawable.product2, "Loose Fit Grey Jeans", "₹1,499"));
        productList.add(new Product(R.drawable.product3, "Light Blue Jeans", "₹899"));
        productList.add(new Product(R.drawable.product4, "Tapered Fit Blue Jeans", "₹2,299"));

        // Add more items

        adapter = new ProductAdapter(this, productList); // ✅ Pass context

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed(); // Exit the app
            return;
        }

        this.doubleBackPressed = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        // Reset flag after 2 seconds
        new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
    }

}
