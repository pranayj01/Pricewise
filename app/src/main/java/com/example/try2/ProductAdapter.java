package com.example.try2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Load image
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .override(140, 160)
                .fitCenter()
                .into(holder.productImage);

        // Set product data
        holder.productName.setText(product.getTitle());
        holder.productPrice.setText(product.getPrice());
        holder.productMrp.setText(product.getMrp());
        holder.productMrp.setPaintFlags(holder.productMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productDiscount.setText(product.getDiscount());

        // Set platform tag
        if (product.getPlatform() != null) {
            holder.productPlatformTag.setText(product.getPlatform());
            // Set different background colors based on platform
            if ("Amazon".equalsIgnoreCase(product.getPlatform())) {
                holder.productPlatformTag.setBackgroundResource(R.drawable.amazon_tag_bg);
            } else {
                holder.productPlatformTag.setBackgroundResource(R.drawable.flipkart_tag_bg);
            }
            holder.productPlatformTag.setVisibility(View.VISIBLE);
        } else {
            holder.productPlatformTag.setVisibility(View.GONE);
        }

        // Handle click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productMrp, productDiscount, productPlatformTag;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productMrp = itemView.findViewById(R.id.product_mrp);
            productDiscount = itemView.findViewById(R.id.product_discount);
            productPlatformTag = itemView.findViewById(R.id.product_platform_tag);
        }
    }
}