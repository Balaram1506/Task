package com.example.task_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupPhone, signupPassword, signupEmail;
    private Button signupButton;
    private TextView loginRedirectText;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Bind Views
        signupPhone = findViewById(R.id.signup_phone);
        signupPassword = findViewById(R.id.signup_password);
        signupEmail = findViewById(R.id.signup_email); // New email field
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Handle Sign-Up Button Click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = signupPhone.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();

                if (phone.isEmpty() || phone.length() < 10) {
                    signupPhone.setError("Enter a valid phone number");
                    return;
                }
                if (password.isEmpty() || password.length() < 6) {
                    signupPassword.setError("Password must be at least 6 characters");
                    return;
                }
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signupEmail.setError("Enter a valid email address");
                    return;
                }

                // Save user details to Firebase Database
                saveUserToDatabase(phone, password, email);
            }
        });

        // Redirect to Login Page
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    // Save user data to Firebase Database
    private void saveUserToDatabase(String phone, String password, String email) {
        databaseReference.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(SignUpActivity.this, "Phone number already registered", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("phone", phone);
                    userMap.put("password", password);
                    userMap.put("email", email); // Add email to the user data

                    databaseReference.child(phone).setValue(userMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, MainPage.class));
                            finish(); // Close SignUpActivity
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed to Save User Data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}