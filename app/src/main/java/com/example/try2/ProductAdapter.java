package com.example.try2;

import android.content.Context;
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
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
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

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .override(140, 160)
                .fitCenter()
                .into(holder.productImage);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productMrp.setText(product.getMrp());
        holder.productMrp.setPaintFlags(holder.productMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productDiscount.setText(product.getDiscount());

        if (product.getPlatform() != null) {
            holder.productPlatformTag.setText(product.getPlatform());
            if ("Amazon".equalsIgnoreCase(product.getPlatform())) {
                holder.productPlatformTag.setBackgroundResource(R.drawable.amazon_tag_bg);
            } else {
                holder.productPlatformTag.setBackgroundResource(R.drawable.flipkart_tag_bg);
            }
            holder.productPlatformTag.setVisibility(View.VISIBLE);
        } else {
            holder.productPlatformTag.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
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
