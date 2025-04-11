package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity); // Make sure this is the correct layout file

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(SearchActivity.this, HomeActivity.class));
                    finish(); // ✅ Close current to prevent back stack
                    return true;
                } else if (id == R.id.nav_search) {
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                    finish(); // ✅ Close current to prevent back stack
                    return true;
                }
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_search);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // ✅ Ensures SearchActivity is removed from back stack
    }
}
