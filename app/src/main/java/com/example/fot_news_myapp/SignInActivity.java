package com.example.fot_news_myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, emailEditText;
    private Button signInButton;
    private TextView haveAccountLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set up the window insets to prevent UI elements from being hidden behind system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_in), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        emailEditText = findViewById(R.id.email);
        signInButton = findViewById(R.id.sign_in_button);
        haveAccountLogin = findViewById(R.id.have_account_login);

        // Set Sign-Up Button Click Listener
        signInButton.setOnClickListener(v -> signUpUser());

        // Set Login Link Click Listener
        haveAccountLogin.setOnClickListener(v -> {
            // Navigate to Login Activity
            Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Firebase Sign Up Method
    private void signUpUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignInActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user using Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // If sign-up is successful, sign the user in
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign-up fails, display a message to the user
                        Exception exception = task.getException();
                        Toast.makeText(SignInActivity.this, "Authentication failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Update UI based on the current user
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Go to the Main Activity after successful sign-up/sign-in
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the SignInActivity
        }
    }
}
