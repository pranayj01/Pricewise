package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    EditText searchInput;
    ImageView searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);  // ✅ Correct layout file name

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchInput = findViewById(R.id.search_input);
        searchIcon = findViewById(R.id.search_icon);

        // Setup bottom navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(SearchActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_search) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        // Trigger search on icon click or keyboard "Search" press
        searchIcon.setOnClickListener(v -> performSearch());
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim();
        if (!query.isEmpty()) {
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);  // ✅ Corrected class name
            intent.putExtra("search_query", query);
            startActivity(intent);
        } else {
            Toast.makeText(SearchActivity.this, "Please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }
}
