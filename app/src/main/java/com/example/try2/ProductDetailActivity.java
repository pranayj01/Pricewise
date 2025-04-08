// File name must be: ProductDetailActivity.java
package com.example.try2;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView title, properties, price, mrp, discount, lowestLabel;
    private ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Bind views
        productImage = findViewById(R.id.product_image);
        title = findViewById(R.id.product_name);
        properties = findViewById(R.id.product_description);
        price = findViewById(R.id.product_price);
        mrp = findViewById(R.id.product_mrp);
        discount = findViewById(R.id.product_discount);
        lowestLabel = findViewById(R.id.lowest_price_label);

        // Strikethrough on MRP
        mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        // Call API
        ProductApi api = ApiClient.getRetrofitInstance().create(ProductApi.class);
        Call<Product> call = api.getProduct(); // If using mock API without ID

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (!isFinishing() && !isDestroyed() && response.isSuccessful() && response.body() != null) {
                    Product product = response.body();

                    title.setText(product.getName());
                    if (product.getProperties() != null) {
                        StringBuilder descBuilder = new StringBuilder();
                        for (String prop : product.getProperties()) {
                            descBuilder.append("• ").append(prop).append("\n");
                        }
                        properties.setText(descBuilder.toString().trim());
                    }

                    price.setText(product.getPrice());
                    mrp.setText(product.getMrp());
                    mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    discount.setText(product.getDiscount());

                    Glide.with(ProductDetailActivity.this)
                            .load(product.getImageUrl())
                            .placeholder(R.drawable.placeholder_image)
                            .into(productImage);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to get product data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
