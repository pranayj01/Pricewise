package com.example.try2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView title, properties, price, mrp, discount, lowestLabel, platform;
    private ImageView productImage;
    private LineChart priceChart;
    private Product product;
    private Button viewOnPlatformButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        priceChart = findViewById(R.id.price_graph);
        productImage = findViewById(R.id.product_image);
        title = findViewById(R.id.product_name);
        properties = findViewById(R.id.product_description);
        price = findViewById(R.id.product_price);
        mrp = findViewById(R.id.product_mrp);
        discount = findViewById(R.id.product_discount);
        lowestLabel = findViewById(R.id.lowest_price_label);
        platform = findViewById(R.id.product_platform);
        viewOnPlatformButton = findViewById(R.id.view_on_platform);

        setupEmptyChart();

        // Get Product object from Intent
        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            bindProductDetails();
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void bindProductDetails() {
        title.setText(product.getName());
        platform.setText(product.getPlatform());
        String platform = (product.getPlatform() != null) ? product.getPlatform().toUpperCase() : "PLATFORM";
        viewOnPlatformButton.setText("VIEW ON " + platform);

        // Set properties
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

        lowestLabel.setVisibility(product.isLowest() ? View.VISIBLE : View.GONE);

        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .override(1024, 1024)
                .centerInside()
                .into(productImage);

        if (product.getProductUrl() != null && !product.getProductUrl().isEmpty()) {
            viewOnPlatformButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getProductUrl()));
                startActivity(intent);
            });
        } else {
            viewOnPlatformButton.setOnClickListener(v -> {
                Toast.makeText(this, product.getPlatform() + " link not available", Toast.LENGTH_SHORT).show();
            });
        }

        fetchPriceHistory();
    }
    private void setupEmptyChart() {
        priceChart.setNoDataText("Loading price history...");
        priceChart.setNoDataTextColor(Color.GRAY);
        priceChart.getDescription().setEnabled(false);
        priceChart.getAxisRight().setEnabled(false);
        priceChart.getLegend().setEnabled(false);
    }

    private void fetchPriceHistory() {
        if (product == null || product.getId() == null) {
            priceChart.setNoDataText("Product information unavailable");
            return;
        }

        ProductApi api = ApiClient.getRetrofitInstance().create(ProductApi.class);
        Call<List<PriceHistoryWrapper>> call = api.getAllPriceHistories();

        call.enqueue(new Callback<List<PriceHistoryWrapper>>() {
            @Override
            public void onResponse(Call<List<PriceHistoryWrapper>> call, Response<List<PriceHistoryWrapper>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (PriceHistoryWrapper wrapper : response.body()) {
                        if (wrapper.getProductId().equals(product.getId())) {
                            updatePriceChart(wrapper.getHistory());
                            return;
                        }
                    }
                    priceChart.setNoDataText("No price history available for this product");
                } else {
                    priceChart.setNoDataText("No price history available");
                }
            }

            @Override
            public void onFailure(Call<List<PriceHistoryWrapper>> call, Throwable t) {
                priceChart.setNoDataText("Error loading price history");
            }
        });
    }

    private void updatePriceChart(List<PriceHistory> priceHistory) {
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < priceHistory.size(); i++) {
            PriceHistory history = priceHistory.get(i);
            entries.add(new Entry(i, history.getPrice()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price History");
        dataSet.setColor(Color.parseColor("#FF9800"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.parseColor("#FF9800"));
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        priceChart.setData(lineData);

        XAxis xAxis = priceChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < priceHistory.size()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
                    return sdf.format(priceHistory.get(index).getDate());
                }
                return "";
            }
        });

        priceChart.invalidate();
    }
}
