package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        loadAdminDetails(); // Load admin details on activity creation
        buttonNavigation();
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(AdminProfileActivity.this, AdminMainActivity.class)));
    }

    private void loadAdminDetails() {
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid(); // Get current logged-in user's ID

        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve and display admin details
                String name = documentSnapshot.getString("FullName"); // Ensure Firestore has a 'fullname' field
                String email = documentSnapshot.getString("UserEmail"); // Ensure Firestore has an 'email' field

                binding.adminName.setText(name != null ? name : "N/A");
                binding.adminEmail.setText(email != null ? email : "N/A");
            } else {
                Toast.makeText(AdminProfileActivity.this, "User details not found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(AdminProfileActivity.this, "Failed to load user details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
