// File: ProductDetailActivity.java
package com.example.try2;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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

        // ✅ Get the product object passed via Intent
        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            // Set data
            title.setText(product.getName());

            // Set bullet-style properties
            if (product.getProperties() != null) {
                StringBuilder descBuilder = new StringBuilder();
                for (String prop : product.getProperties()) {
                    descBuilder.append("• ").append(prop).append("\n");
                }
                properties.setText(descBuilder.toString().trim());
            }

            price.setText(product.getPrice());
            mrp.setText(product.getMrp());
            discount.setText(product.getDiscount());

            // Set lowest price label
            if (product.isLowest()) {
                lowestLabel.setText("Lowest Price");
                lowestLabel.setVisibility(TextView.VISIBLE);
            } else {
                lowestLabel.setVisibility(TextView.GONE);
            }

            // Load image with bitmap size optimization to prevent "Canvas: trying to draw too large bitmap" error
            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .override(1024, 1024) // Resize large images
                    .centerInside()
                    .into(productImage);
            Button viewOnFlipkartButton = findViewById(R.id.view_on_platform);

            if (product.getProductUrl() != null && !product.getProductUrl().isEmpty()) {
                viewOnFlipkartButton.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getProductUrl()));
                    startActivity(intent);
                });
            } else {
                viewOnFlipkartButton.setOnClickListener(v -> {
                    Toast.makeText(this, "Flipkart link not available", Toast.LENGTH_SHORT).show();
                });
            }

        } else {
            Toast.makeText(this, "Product not found.", Toast.LENGTH_SHORT).show();
        }
    }
}
