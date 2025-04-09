package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private MaterialButton loginButton;
    private TextView createAccount;
    private boolean doubleBackPressed = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        createAccount = findViewById(R.id.create_account);

        // Optimize images loaded from layout
        optimizeLayoutImages();

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Optimize images loaded directly from layout XML
     * This prevents "Canvas: trying to draw too large bitmap" errors
     */
    private void optimizeLayoutImages() {
        try {
            // Find all ImageViews with direct drawable references in the layout
            ImageView googleIcon = findViewById(R.id.google_icon);
            ImageView flipkartIcon = findViewById(R.id.flipkart_icon);
            ImageView amazonIcon = findViewById(R.id.amazon_icon);

            // Use BitmapUtils to load images efficiently
            if (googleIcon != null) {
                BitmapUtils.loadDrawableIntoImageView(this, googleIcon, R.drawable.google);
            }

            if (flipkartIcon != null) {
                BitmapUtils.loadDrawableIntoImageView(this, flipkartIcon, R.drawable.flipcart);
            }

            if (amazonIcon != null) {
                BitmapUtils.loadDrawableIntoImageView(this, amazonIcon, R.drawable.amazon);
            }
        } catch (Exception e) {
            // Log error but don't crash
            e.printStackTrace();
        }
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed();
            return;
        }

        this.doubleBackPressed = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
    }
}
