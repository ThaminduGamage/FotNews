package com.example.fot_news_myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, emailEditText;
    private Button signInButton;
    private TextView haveAccountLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        String username = usernameEditText.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
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

                        // Add user information to Firebase Realtime Database
                        if (user != null) {
                            saveUserToDatabase(user, username, email);
                        }

                    } else {
                        // If sign-up fails, display a message to the user
                        Exception exception = task.getException();
                        Toast.makeText(SignInActivity.this, "Authentication failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Save user data to Firebase Realtime Database
    private void saveUserToDatabase(FirebaseUser user, String username, String email) {
        // Create a User object
        User newUser = new User(username, email);

        // Save the user data in Realtime Database under the user's UID
        mDatabase.child("users").child(user.getUid()).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // After successful sign-up, go to the Login Activity
                        goToLoginActivity();
                    } else {
                        Toast.makeText(SignInActivity.this, "Failed to save user info.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Navigate to Login Activity
    private void goToLoginActivity() {
        // Create an Intent to navigate to LoginActivity
        Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current SignInActivity
    }

    // Update UI (Not needed anymore for this use case)
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Go to the Login Activity after successful registration
            goToLoginActivity();
        }
    }
}

// User model class to store user information
class User {
    public String username;
    public String email;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
