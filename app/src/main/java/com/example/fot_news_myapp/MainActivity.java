package com.example.fot_news_myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton; // Make sure this import is present

import androidx.activity.EdgeToEdge; // Keep if your project uses EdgeToEdge
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets; // Keep if your project uses Insets
import androidx.core.view.ViewCompat; // Keep if your project uses ViewCompat
import androidx.core.view.WindowInsetsCompat; // Keep if your project uses WindowInsetsCompat
import androidx.fragment.app.Fragment; // Still needed for other fragments

//import com.example.fot_news_myapp.fragments.AcademicFragment;
import com.example.fot_news_myapp.fragments.AcademicFragment;
import com.example.fot_news_myapp.fragments.EventsFragment;
import com.example.fot_news_myapp.fragments.SportsFragment;
// import com.example.fot_news_myapp.fragments.UserFragment; // REMOVED: UserFragment will no longer be loaded here
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private Fragment sportsFragment = new SportsFragment();
    private Fragment academicFragment = new AcademicFragment();
    private Fragment eventsFragment = new EventsFragment();
    // Assuming this fragment is correctly linked, ensure the package name is accurate.
    // If 'com.example.fotconnect.fragments.DevInfoFragment' is indeed from another module/package, keep it.
    // Otherwise, it might be 'com.example.fot_news_myapp.fragments.DevInfoFragment'.
    private Fragment devInfoFragment = new com.example.fotconnect.fragments.DevInfoFragment();
    // Removed: private Fragment userFragment = new UserFragment(); // No longer needed as UserInfoActivity is launched directly

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This is typically for full-screen and edge-to-edge display, keep if intended for MainActivity
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // Set the layout for MainActivity

        // Apply window insets for system bars if using EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // Handle bottom navigation item selection
            if (id == R.id.nav_sports) {
                loadFragment(sportsFragment);
                return true;
            } else if (id == R.id.nav_academic) {
                loadFragment(academicFragment);
                return true;
            } else if (id == R.id.nav_events) {
                loadFragment(eventsFragment);
                return true;
            } else if (id == R.id.dev_info) {
                loadFragment(devInfoFragment);
                return true;
            }
            return false;
        });

        // Handle ImageButton click to open UserInfoActivity (as a separate activity)
        // Ensure that the ID 'imageButton4' matches the ID in your activity_main.xml for the ImageButton
        ImageButton imageButton = findViewById(R.id.imageButton4);
        imageButton.setOnClickListener(v -> {
            // Create an Intent to start userinfoActivity
            Intent intent = new Intent(MainActivity.this, userinfoActivity.class);
            startActivity(intent); // Launch the userinfoActivity
            // Optional: If you want MainActivity to finish and not be in the back stack
            // after launching userinfoActivity, uncomment the line below:
            // finish();
        });

        // Load initial fragment if not restored from a saved state
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_academic); // Select Academic as default
            loadFragment(academicFragment); // Default fragment (e.g., academic)
        }
    }

    // Method to load fragments dynamically into a container (e.g., FrameLayout)
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)  // Replace current fragment with the new one
                .commit();
    }

    // Removed: getUserFragment() and setUserFragment() methods as UserFragment is no longer managed by MainActivity.
    // public Fragment getUserFragment() { return userFragment; }
    // public void setUserFragment(Fragment userFragment) { this.userFragment = userFragment; }
}