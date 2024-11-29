package com.example.groceryshop01.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.groceryshop01.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base); // Base layout containing BottomNavigationView
        setupBottomNavigation();
    }

    protected void setContentLayout(int layoutResID) {
        FrameLayout frameLayout = findViewById(R.id.activityContent);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                if (!(this instanceof MainActivity)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                return true;
            } else if (itemId == R.id.nav_cart) {
                if (!(this instanceof CartActivity)) {
                    startActivity(new Intent(this, CartActivity.class));
                    finish();
                }
                return true;
            } else if (itemId == R.id.nav_profile) {
                if (!(this instanceof ProfileActivity)) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish();
                }
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBottomNavigationSelection();
    }

    private void updateBottomNavigationSelection() {
        if (this instanceof MainActivity) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else if (this instanceof CartActivity) {
            bottomNavigationView.setSelectedItemId(R.id.nav_cart);
        } else if (this instanceof ProfileActivity) {
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }
    }
}
