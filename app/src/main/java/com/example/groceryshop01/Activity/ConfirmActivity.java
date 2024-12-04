package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Domain.PendingOrderModel;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityConfirmBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ConfirmActivity extends BaseActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private TextView userNameTxt, userEmailTxt, deliveryAddressTxt, dateTxt, paymentMethodTxt, totalTxt, storeNameTxt;
    private ManagmentCart managmentCart;
    private ActivityConfirmBinding binding;
    private double total; // Declare total at the class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        managmentCart = new ManagmentCart(this);

        userNameTxt = findViewById(R.id.userNameTxt);
        userEmailTxt = findViewById(R.id.userEmailTxt);
        deliveryAddressTxt = findViewById(R.id.deliveryAddressTxt);
        dateTxt = findViewById(R.id.dateTxt);
        paymentMethodTxt = findViewById(R.id.paymentMethodTxt);
        totalTxt = findViewById(R.id.totalTxt);
        storeNameTxt = findViewById(R.id.storeNameTxt);

        loadUserData();
        loadCartDetails();
        statusBarColor();
        buttonNavigation();
    }

    private void statusBarColor() {
        Window window = ConfirmActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ConfirmActivity.this, R.color.dark_green));
    }

    private void loadUserData() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        firestore.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userNameTxt.setText(documentSnapshot.getString("FullName"));
                        userEmailTxt.setText(documentSnapshot.getString("UserEmail"));
                        deliveryAddressTxt.setText(documentSnapshot.getString("UserAddress"));
                    }
                })
                .addOnFailureListener(e -> {
                    userNameTxt.setText("N/A");
                    userEmailTxt.setText("N/A");
                    deliveryAddressTxt.setText("N/A");
                });
    }

    private void loadCartDetails() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        dateTxt.setText(currentDate);

        String selectedMethod = getIntent().getStringExtra("selectedPaymentMethod");
        if (selectedMethod != null) {
            paymentMethodTxt.setText(selectedMethod);
        }

        double percentTax = 0.02; // 2% tax
        double delivery = 10; // Flat delivery fee
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;
        double tax = Math.round(itemTotal * percentTax * 100) / 100.0;
        total = Math.round((itemTotal + tax + delivery) * 100) / 100.0; // Assign total to class variable

        totalTxt.setText("Tk " + total);
        storeNameTxt.setText("GreenGrocer");

        // Creating a PendingOrderModel instance to set the total
        PendingOrderModel pendingOrder = new PendingOrderModel();
        pendingOrder.setTotal(total);
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(ConfirmActivity.this, CartActivity.class)));
        binding.OrderBtn.setOnClickListener(v -> {
            String customerName = userNameTxt.getText().toString();
            String customerId = auth.getCurrentUser().getUid();

            List<PendingOrderModel.Item> cartItems = new ArrayList<>();
            for (ItemsModel item : managmentCart.getListCart()) {
                cartItems.add(new PendingOrderModel.Item(item.getName(), item.getQuantity()));
            }

            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty. Cannot place order.", Toast.LENGTH_SHORT).show();
                return;
            }

            String orderId = firebaseDatabase.getReference("Orders").push().getKey(); // Generate a new order ID
            PendingOrderModel pendingOrder = new PendingOrderModel(orderId, customerName, cartItems, "pending", "not received", total);
            pendingOrder.setTotal(total);

            // Store the order data in Firebase Realtime Database
            firebaseDatabase.getReference("Orders").child(orderId)
                    .setValue(pendingOrder)
                    .addOnSuccessListener(aVoid -> {
                        updateItemQuantities(cartItems); // Call the method to update quantities
                        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void updateItemQuantities(List<PendingOrderModel.Item> cartItems) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("categories");

        for (PendingOrderModel.Item item : cartItems) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                        for (DataSnapshot itemSnapshot : categorySnapshot.getChildren()) {
                            if (itemSnapshot.child("name").getValue(String.class).equals(item.getName())) {
                                long currentQuantity = itemSnapshot.child("quantity").getValue(Long.class);
                                long updatedQuantity = currentQuantity - item.getQuantity();
                                itemSnapshot.getRef().child("quantity").setValue(Math.max(updatedQuantity, 0));
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ConfirmActivity", "Failed to update quantity: " + error.getMessage());
                }
            });
        }
    }
}
