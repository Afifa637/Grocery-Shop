package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityConfirmBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ConfirmActivity extends BaseActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private TextView userNameTxt, userEmailTxt, deliveryAddressTxt, dateTxt, paymentMethodTxt, totalTxt, storeNameTxt;
    private ManagmentCart managmentCart;
    private ActivityConfirmBinding binding;// Assume you already have this class for cart management.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());


        // Initialize Firebase and UI elements
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        managmentCart = new ManagmentCart(this); // Adjust based on your implementation.

        userNameTxt = findViewById(R.id.userNameTxt);
        userEmailTxt = findViewById(R.id.userEmailTxt);
        deliveryAddressTxt = findViewById(R.id.deliveryAddressTxt);
        dateTxt = findViewById(R.id.dateTxt);
        paymentMethodTxt = findViewById(R.id.paymentMethodTxt);
        totalTxt = findViewById(R.id.totalTxt);
        storeNameTxt = findViewById(R.id.storeNameTxt);

        // Populate receipt details
        loadUserData();
        loadCartDetails();
        buttonNavigation();
    }

    private void loadUserData() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        firestore.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Update UI with user details
                        userNameTxt.setText(documentSnapshot.getString("FullName"));
                        userEmailTxt.setText(documentSnapshot.getString("UserEmail"));
                        deliveryAddressTxt.setText(documentSnapshot.getString("UserAddress")); // Ensure this field exists in Firestore.
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    userNameTxt.setText("N/A");
                    userEmailTxt.setText("N/A");
                    deliveryAddressTxt.setText("N/A");
                });
    }

    private void loadCartDetails() {
        // Update date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        dateTxt.setText(currentDate);

        // Payment method (hardcoded for now, replace with dynamic data if needed)

        String selectedMethod = getIntent().getStringExtra("selectedPaymentMethod");
        if (selectedMethod != null) {
            paymentMethodTxt.setText(selectedMethod); // Set the method in the TextView
        }

        // Calculate and update total
        double percentTax = 0.02; // 2% tax
        double delivery = 10; // Flat delivery fee
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;
        double tax = Math.round(itemTotal * percentTax * 100) / 100.0;
        double total = Math.round((itemTotal + tax + delivery) * 100) / 100.0;

        totalTxt.setText("Tk " + total);
        storeNameTxt.setText("GreenGrocer"); // Replace with dynamic store name if required.
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(ConfirmActivity.this, CartActivity.class)));
        binding.PayBillBtn.setOnClickListener(v -> {
                updateMoneyStatus("received");
            Toast.makeText(this, "Bill is Received!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ConfirmActivity.this, CartActivity.class));
        });
    }

    private void updateMoneyStatus(String status) {
        firestore.collection("Users").whereEqualTo("isCustomer", "1") .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                document.getReference().update("moneyStatus", status)
                        .addOnSuccessListener(aVoid -> Log.d("ConfirmActivity", "Status updated"))
                        .addOnFailureListener(e -> Log.e("ConfirmActivity", "Error updating status", e));
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error fetching users for status update.", Toast.LENGTH_SHORT).show());
    }

}
