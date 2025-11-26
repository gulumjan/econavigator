package com.example.econavigator.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.econavigator.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.econavigator.fragments.HomeFragment;
import com.example.econavigator.fragments.MapFragment;
import com.example.econavigator.fragments.GameFragment;
import com.example.econavigator.fragments.LeaderboardFragment;
import com.example.econavigator.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Setup bottom navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.navigation_map) {
                fragment = new MapFragment();
            } else if (itemId == R.id.navigation_game) {
                fragment = new GameFragment();
            } else if (itemId == R.id.navigation_leaderboard) {
                fragment = new LeaderboardFragment();
            } else if (itemId == R.id.navigation_profile) {
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                return loadFragment(fragment);
            }
            return false;
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            return true;
        }
        return false;
    }
}