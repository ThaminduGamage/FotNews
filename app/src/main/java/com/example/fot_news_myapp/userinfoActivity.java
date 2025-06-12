package com.example.fot_news_myapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class userinfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        // Find the Edit Profile button and set an OnClickListener
        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog();
            }
        });

        // Find the Sign Out button and set an OnClickListener
        findViewById(R.id.btnSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignOutDialog();
            }
        });
    }

    private void openEditDialog() {
        // Create a dialog
        Dialog dialog = new Dialog(userinfoActivity.this);
        dialog.setContentView(R.layout.editdilog);  // Set the custom layout for the dialog

        // Find the buttons and set listeners
        Button okButton = dialog.findViewById(R.id.ok_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);

        // Set the EditTexts with the current values (you can set dynamic values from your model)
        EditText usernameEditText = dialog.findViewById(R.id.username);
        EditText emailEditText = dialog.findViewById(R.id.email);
        EditText dobEditText = dialog.findViewById(R.id.dob);

        // Set current values in the EditTexts (this is just for example, you can get the values dynamically)
        usernameEditText.setText("Kavindu");
        emailEditText.setText("2022t01569@stu.cmb.ac.lk");
        dobEditText.setText("2002/10/25");

        // Handle button clicks
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the EditTexts and perform any action (e.g., save changes)
                String updatedUsername = usernameEditText.getText().toString();
                String updatedEmail = emailEditText.getText().toString();
                String updatedDob = dobEditText.getText().toString();

                // Do something with the updated data (e.g., save or show a toast)
                dialog.dismiss();  // Close the dialog after saving the data
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  // Close the dialog if canceled
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void openSignOutDialog() {
        // Create a dialog for sign-out confirmation
        final Dialog signOutDialog = new Dialog(userinfoActivity.this);
        signOutDialog.setContentView(R.layout.signout_box);  // Your custom layout for sign-out dialog

        // Find the buttons and set listeners
        Button okButton = signOutDialog.findViewById(R.id.ok_button);
        Button cancelButton = signOutDialog.findViewById(R.id.cancel_button);

        // Set the dialog text (optional)
        TextView messageText = signOutDialog.findViewById(R.id.textViewMessage);
        messageText.setText("Really want to sign out?");

        // Handle OK button click
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the sign-out logic here (e.g., log out the user)
                signOutDialog.dismiss();  // Close the dialog after sign-out
            }
        });

        // Handle Cancel button click
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutDialog.dismiss();  // Close the dialog if canceled
            }
        });

        // Show the dialog
        signOutDialog.show();
    }
}
