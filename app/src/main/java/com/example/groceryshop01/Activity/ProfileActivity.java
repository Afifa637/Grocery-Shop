package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());

        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        // Load user data into the fields
        loadUserData();

        // Set default to non-editable
        setEditable(false);

        binding.editBtn.setOnClickListener(v -> {
            isEditable = !isEditable;
            setEditable(isEditable);
            if (isEditable) {
                binding.userName.requestFocus(); // Focus on the first editable field
            }
        });

        binding.saveinfoBtn.setOnClickListener(v -> {
            // Save the updated data
            saveUserData();
        });

        binding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Sets the editable state for user fields and visibility of save button.
     *
     * @param editable true to enable editing, false to disable
     */
    private void setEditable(boolean editable) {
        binding.userName.setEnabled(editable);
        binding.userEmail.setEnabled(editable);
        binding.userAddr.setEnabled(editable);
        binding.userPass.setEnabled(editable);
        binding.saveinfoBtn.setVisibility(editable ? View.VISIBLE : View.GONE);
    }

    private void loadUserData() {
        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                binding.userName.setText(documentSnapshot.getString("FullName"));
                binding.userEmail.setText(documentSnapshot.getString("UserEmail"));
                binding.userAddr.setText(documentSnapshot.getString("UserAddress")); // Ensure this field exists in Firestore
                binding.userPass.setText("******"); // Mask the password for display
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        });
    }

    private void saveUserData() {
        String fullName = binding.userName.getText().toString();
        String email = binding.userEmail.getText().toString();
        String address = binding.userAddr.getText().toString();

        // Create a map with the updated data
        Map<String, Object> userData = new HashMap<>();
        userData.put("FullName", fullName);
        userData.put("UserEmail", email);
        userData.put("UserAddress", address);

        // Update the Firestore document
        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.update(userData).addOnSuccessListener(unused -> {
            Toast.makeText(this, "User info updated", Toast.LENGTH_SHORT).show();
            isEditable = false;
            setEditable(false);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to update user info", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        });
    }
}
