package com.example.fot_news_myapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fot_news_myapp.LoginActivity;  // Corrected the import
import com.example.fot_news_myapp.R;  // Corrected the import
import com.google.firebase.auth.FirebaseAuth; // This import is correct, but requires the dependency

public class UserFragment extends Fragment {

    private FirebaseAuth mAuth; // Declare FirebaseAuth instance

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_userinfo, container, false);

        // Initialize Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Find the Sign Out button by its ID
        Button btnSignOut = view.findViewById(R.id.btnSignOut);

        // Set an OnClickListener for the Sign Out button
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to show the logout confirmation dialog
                showLogoutConfirmationDialog();
            }
        });

        return view;
    }

    /**
     * Displays an AlertDialog to confirm user logout.
     * If "Ok" is clicked, it signs out the user, shows a toast, and redirects to LoginActivity.
     */
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Sign Out") // Set the dialog title
                .setMessage("Are you sure you want to sign out?") // Set the dialog message
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Ok", perform sign out
                        mAuth.signOut(); // Sign out the current user from Firebase
                        Toast.makeText(getContext(), "Signed out successfully.", Toast.LENGTH_SHORT).show();

                        // Create an Intent to navigate to LoginActivity
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        // Set flags to clear the back stack and start a new task
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent); // Start the LoginActivity
                        getActivity().finish(); // Finish the current activity (MainActivity or any activity holding this fragment)
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Cancel", dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .show(); // Display the dialog
    }
}
