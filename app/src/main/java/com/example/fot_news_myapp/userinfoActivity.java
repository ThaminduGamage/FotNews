package com.example.fot_news_myapp;

import android.app.Dialog;
import android.content.Intent; // Import for Intent to redirect after sign out
import android.os.Bundle;
import android.util.Log; // Used for logging errors
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull; // For @NonNull annotations
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener; // For task completion listeners
import com.google.android.gms.tasks.Task; // For task results
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userinfoActivity extends AppCompatActivity {

    private static final String TAG = "UserInfoActivity"; // Tag for Logcat
    private TextView usernameTextView, emailTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo); // Ensure this points to your layout file

        // Initialize Firebase Authentication and Realtime Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components by finding them by their IDs
        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);

        // Get the current logged-in user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Fetch the user data from the Firebase Realtime Database
            fetchUserData(user.getUid());
        } else {
            // User is not logged in, display a message and potentially redirect
            Toast.makeText(userinfoActivity.this, "User is not logged in. Please sign in.", Toast.LENGTH_LONG).show();
            // Optional: Redirect to login activity
            // startActivity(new Intent(userinfoActivity.this, LoginActivity.class));
            // finish(); // Close current activity
        }

        // Set up the Edit Profile button listener
        findViewById(R.id.btnSignIn).setOnClickListener(v -> openEditDialog());

        // Set up the Sign Out button listener
        findViewById(R.id.btnSignOut).setOnClickListener(v -> openSignOutDialog());
    }

    /**
     * Fetches user data (username and email) from Firebase Realtime Database
     * for the given userId and updates the corresponding TextViews in the UI.
     * This uses addListenerForSingleValueEvent to read data once.
     *
     * @param userId The unique ID of the Firebase user.
     */
    private void fetchUserData(String userId) {
        Log.d(TAG, "Fetching user data for UID: " + userId);
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data exists for the user, retrieve username and email
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // Update the UI TextViews. Handle potential null values for data fields.
                    usernameTextView.setText("Username : " + (username != null ? username : "N/A"));
                    emailTextView.setText("Email : " + (email != null ? email : "N/A"));
                    Log.d(TAG, "User data fetched successfully: Username=" + username + ", Email=" + email);
                } else {
                    // No data found for the user in the database
                    Toast.makeText(userinfoActivity.this, "User data not found in database. Consider updating your profile.", Toast.LENGTH_LONG).show();
                    usernameTextView.setText("Username : Not Set");
                    emailTextView.setText("Email : Not Set");
                    Log.d(TAG, "No data found for user UID: " + userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log and toast error if data retrieval fails due to permissions or network issues
                Log.e(TAG, "Failed to load user data: " + databaseError.getMessage());
                Toast.makeText(userinfoActivity.this, "Failed to load user data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                usernameTextView.setText("Username : Error");
                emailTextView.setText("Email : Error");
            }
        });
    }

    /**
     * Opens a custom dialog to allow the user to edit their profile information.
     * Assumes 'editdilog.xml' exists with EditTexts (username, email) and Buttons (ok_button, cancel_button).
     */
    private void openEditDialog() {
        Log.d(TAG, "Opening edit profile dialog.");
        final Dialog dialog = new Dialog(userinfoActivity.this);
        dialog.setContentView(R.layout.editdilog); // Set the custom layout for the dialog

        // Initialize dialog UI components
        EditText usernameEditText = dialog.findViewById(R.id.username);
        EditText emailEditText = dialog.findViewById(R.id.email);
        Button okButton = dialog.findViewById(R.id.ok_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);

        // Populate EditTexts with current values (removing the prefix)
        usernameEditText.setText(usernameTextView.getText().toString().replace("Username : ", "").trim());
        emailEditText.setText(emailTextView.getText().toString().replace("Email : ", "").trim());

        // Set up click listener for the OK button (to save changes)
        okButton.setOnClickListener(v -> {
            String updatedUsername = usernameEditText.getText().toString().trim();
            String updatedEmail = emailEditText.getText().toString().trim();

            // Input validation
            if (updatedUsername.isEmpty()) {
                Toast.makeText(userinfoActivity.this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
                usernameEditText.setError("Username is required!");
                return; // Prevent dialog dismissal if validation fails
            }
            if (updatedEmail.isEmpty()) {
                Toast.makeText(userinfoActivity.this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
                emailEditText.setError("Email is required!");
                return;
            }
            // Add more robust email validation (e.g., using Patterns.EMAIL_ADDRESS.matcher(email).matches()) if needed

            // If validation passes, update the user profile in Firebase
            updateUserProfile(updatedUsername, updatedEmail);
            dialog.dismiss(); // Dismiss the dialog after initiating update
        });

        // Set up click listener for the Cancel button
        cancelButton.setOnClickListener(v -> {
            Log.d(TAG, "Edit profile dialog cancelled.");
            dialog.dismiss(); // Simply close the dialog
        });

        dialog.show(); // Show the dialog
    }

    /**
     * Updates the user's profile information in the Firebase Realtime Database.
     * Updates username and email fields for the current user's data node.
     *
     * @param username The new username.
     * @param email The new email.
     */
    private void updateUserProfile(String username, String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = mDatabase.child("users").child(user.getUid());

            // Update username
            userRef.child("username").setValue(username)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                usernameTextView.setText("Username : " + username);
                                Toast.makeText(userinfoActivity.this, "Username updated.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Username updated successfully to: " + username);
                            } else {
                                Toast.makeText(userinfoActivity.this, "Failed to update username.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Error updating username: " + task.getException().getMessage());
                            }
                        }
                    });

            // Update email
            userRef.child("email").setValue(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                emailTextView.setText("Email : " + email);
                                Toast.makeText(userinfoActivity.this, "Email updated.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email updated successfully to: " + email);
                            } else {
                                Toast.makeText(userinfoActivity.this, "Failed to update email.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Error updating email: " + task.getException().getMessage());
                            }
                        }
                    });

            // If you also need to update the email in Firebase Authentication:
            // user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            //     @Override
            //     public void onComplete(@NonNull Task<Void> task) {
            //         if (task.isSuccessful()) {
            //             Log.d(TAG, "Firebase Auth email updated.");
            //         } else {
            //             Log.e(TAG, "Error updating Firebase Auth email: " + task.getException().getMessage());
            //             // Handle specific errors like "requires-recent-login"
            //         }
            //     }
            // });

        } else {
            Toast.makeText(userinfoActivity.this, "User not logged in to update profile.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Attempted to update profile but no user was logged in.");
        }
    }

    /**
     * Opens a confirmation dialog for signing out the user.
     * Assumes 'signout_box.xml' exists with Buttons (ok_button, cancel_button).
     */
    private void openSignOutDialog() {
        Log.d(TAG, "Opening sign out confirmation dialog.");
        final Dialog signOutDialog = new Dialog(userinfoActivity.this);
        signOutDialog.setContentView(R.layout.signout_box); // Set the custom layout for the dialog

        // Initialize dialog UI components
        Button okButton = signOutDialog.findViewById(R.id.ok_button);
        Button cancelButton = signOutDialog.findViewById(R.id.cancel_button);

        // Set up click listener for the OK button (to confirm sign out)
        okButton.setOnClickListener(v -> {
            mAuth.signOut(); // Perform Firebase sign out
            Toast.makeText(userinfoActivity.this, "Signed out successfully.", Toast.LENGTH_LONG).show();
            Log.i(TAG, "User signed out.");
            signOutDialog.dismiss(); // Dismiss the dialog

            // Redirect user to the login/main activity after sign out
            // Replace LoginActivity.class with your actual login activity class
            Intent intent = new Intent(userinfoActivity.this, LoginActivity.class); // <-- IMPORTANT: Replace LoginActivity.class with your actual login activity name
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
            finish(); // Finish this activity so user can't navigate back to it
        });

        // Set up click listener for the Cancel button
        cancelButton.setOnClickListener(v -> {
            Log.d(TAG, "Sign out dialog cancelled.");
            signOutDialog.dismiss(); // Simply close the dialog
        });

        signOutDialog.show(); // Show the dialog
    }
}