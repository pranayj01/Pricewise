package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LinearLayout logoutLayout;
    private LinearLayout deleteAccLayout;
    private LinearLayout changePasswordLayout; // Added change password layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Setup the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // Initialize the layouts (LinearLayouts)
        logoutLayout = findViewById(R.id.logout_layout);
        deleteAccLayout = findViewById(R.id.delete_acc);
        changePasswordLayout = findViewById(R.id.change_password); // Initialize change password layout

        // Set OnClickListener for logout layout
        logoutLayout.setOnClickListener(v -> logoutUser());
        // Set OnClickListener for delete account layout
        deleteAccLayout.setOnClickListener(v -> deleteAccount());
        // Set OnClickListener for change password layout
        changePasswordLayout.setOnClickListener(v -> changePassword());
    }

    private void logoutUser() {
        // Sign out from Firebase
        mAuth.signOut();

        // Navigate to the MainActivity (Login screen)
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clears the back stack
        startActivity(intent);
        finish();
    }

    private void deleteAccount() {
        // Ensure the user is logged in
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();

                            // Navigate to the login screen after account deletion
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clears the back stack
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Account deletion failed. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(ProfileActivity.this, "No user is logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to change password
    private void changePassword() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Check if the user is using Google authentication
            boolean isGoogleAuth = false;
            for (int i = 0; i < user.getProviderData().size(); i++) {
                if (user.getProviderData().get(i).getProviderId().equals("google.com")) {
                    isGoogleAuth = true;
                    break;
                }
            }

            if (isGoogleAuth) {
                // If Google authentication, show message
                Toast.makeText(ProfileActivity.this, "You cannot change your password if using Google authentication.", Toast.LENGTH_LONG).show();
            } else {
                // Send a password reset email for email/password users
                mAuth.sendPasswordResetEmail(user.getEmail())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Password reset email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Log error for debugging
                                Exception exception = task.getException();
                                if (exception != null) {
                                    Toast.makeText(ProfileActivity.this, "Failed to send password reset email. " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else {
            Toast.makeText(ProfileActivity.this, "No user is logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // ✅ Ensures ProfileActivity is removed from the back stack
    }
}
