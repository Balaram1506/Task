package com.example.task_1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.gbuttons.GoogleSignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText loginPhone, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    private GoogleSignInButton googleBtn;

    private DatabaseReference databaseReference;
    private GoogleSignInClient gClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPhone = findViewById(R.id.login_phone);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);
        googleBtn = findViewById(R.id.googleBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Set up Google Sign-In options
        GoogleSignInOptions gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        // Handle login with phone number and password
        loginButton.setOnClickListener(view -> {
            String phone = loginPhone.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (phone.isEmpty() || phone.length() < 10) {
                loginPhone.setError("Enter a valid phone number");
                return;
            }
            if (password.isEmpty() || password.length() < 6) {
                loginPassword.setError("Password must be at least 6 characters");
                return;
            }

            loginWithPhoneAndPassword(phone, password);
        });

        // Redirect to sign-up page
        signupRedirectText.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        // Handle Google Sign-In
        googleBtn.setOnClickListener(view -> {
            Intent signInIntent = gClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    // Handle phone number and password login
    private void loginWithPhoneAndPassword(String phone, String password) {
        databaseReference.child(phone).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists() && snapshot.child("password").getValue(String.class).equals(password)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainPage.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Database Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle Google Sign-In results
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            validateGoogleAccount(account);
                        } catch (ApiException e) {
                            Toast.makeText(LoginActivity.this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    // Validate Google account email with Firebase phone number
    private void validateGoogleAccount(GoogleSignInAccount account) {
        if (account != null) {
            String email = account.getEmail();
            databaseReference.orderByChild("phone").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean emailMatches = false;
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String registeredEmail = userSnapshot.child("email").getValue(String.class);
                        if (email != null && email.equalsIgnoreCase(registeredEmail)) {
                            emailMatches = true;
                            break;
                        }
                    }

                    if (emailMatches) {
                        Toast.makeText(LoginActivity.this, "Google Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainPage.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "No matching account found for this email", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}