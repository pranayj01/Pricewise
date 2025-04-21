package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private MaterialButton signupButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        optimizeLayoutImages();

        mAuth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.signup_email_input);
        passwordInput = findViewById(R.id.signup_password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(v -> registerUser());

        TextView loginRedirect = findViewById(R.id.login_redirect);
        loginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });
    }

    private void optimizeLayoutImages() {
        try {
            ImageView googleIcon = findViewById(R.id.signup_google_icon);
            ImageView flipkartIcon = findViewById(R.id.signup_flipkart_icon);
            ImageView amazonIcon = findViewById(R.id.signup_amazon_icon);

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
            e.printStackTrace();
        }
    }
    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser == null) {
                    Toast.makeText(SignupActivity.this, "Signup failed: user is null", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseUser.sendEmailVerification().addOnCompleteListener(emailTask -> {
                    if (emailTask.isSuccessful()) {
                        String userId = firebaseUser.getUid();

                        HashMap<String, String> userData = new HashMap<>();
                        userData.put("name", name);
                        userData.put("email", email);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(userId)
                                .setValue(userData)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Signup successful! Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                                        mAuth.signOut(); // Prevent login before email is verified
                                        // Redirect to MainActivity instead of MainActivity
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Failed to save user info", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(SignupActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(SignupActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
