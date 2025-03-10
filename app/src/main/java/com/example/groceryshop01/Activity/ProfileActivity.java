package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

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

        loadUserData();
        setEditable(false);

        binding.editBtn.setOnClickListener(v -> {
            isEditable = !isEditable;
            setEditable(isEditable);
            if (isEditable) {
                binding.userName.requestFocus();
            }
        });

        binding.saveinfoBtn.setOnClickListener(v -> {
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
        statusBarColor();
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

    private void statusBarColor() {
        Window window = ProfileActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ProfileActivity.this, R.color.dark_green));
    }

    private void loadUserData() {
        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                binding.userName.setText(documentSnapshot.getString("FullName"));
                binding.userEmail.setText(documentSnapshot.getString("UserEmail"));
                binding.userAddr.setText(documentSnapshot.getString("UserAddress"));
                binding.userPass.setText("******");
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

        Map<String, Object> userData = new HashMap<>();
        userData.put("FullName", fullName);
        userData.put("UserEmail", email);
        userData.put("UserAddress", address);

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
