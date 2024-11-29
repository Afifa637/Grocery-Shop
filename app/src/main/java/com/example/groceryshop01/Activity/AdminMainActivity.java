package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryshop01.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        com.example.groceryshop01.databinding.ActivityAdminMainBinding binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the click listener for the addMenu button
        binding.addMenu.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        binding.orderDisBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, OrderDispatchActivity.class);
            startActivity(intent);
        });

        binding.logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, Login.class);
            startActivity(intent);
        });

        binding.allItem.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, AllItemActivity.class);
            startActivity(intent);
        });

        binding.adminProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, AdminProfileActivity.class);
            startActivity(intent);
        });
    }
}
