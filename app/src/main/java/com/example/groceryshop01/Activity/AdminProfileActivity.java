package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityAdminProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AdminProfileActivity extends AppCompatActivity {

    private ActivityAdminProfileBinding binding;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAdminProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        loadAdminDetails();
        statusBarColor();
        buttonNavigation();
    }

    private void statusBarColor() {
        Window window = AdminProfileActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AdminProfileActivity.this, R.color.dark_green));
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(AdminProfileActivity.this, AdminMainActivity.class)));
    }

    private void loadAdminDetails() {
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve and display admin details
                String name = documentSnapshot.getString("FullName");
                String email = documentSnapshot.getString("UserEmail");

                binding.adminName.setText(name != null ? name : "N/A");
                binding.adminEmail.setText(email != null ? email : "N/A");
            } else {
                Toast.makeText(AdminProfileActivity.this, "User details not found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(AdminProfileActivity.this, "Failed to load user details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
