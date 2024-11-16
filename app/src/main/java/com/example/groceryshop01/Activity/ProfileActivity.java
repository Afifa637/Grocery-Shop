package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityAdminBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends BaseActivity {

    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());

        binding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        buttonNavigation();
    }

        private void buttonNavigation() {
            binding.backBtn.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, MainActivity.class)));
    }
}