package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.groceryshop01.Adapter.CartAdapter;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {

    private ManagmentCart managmentCart;
    private ActivityCartBinding binding;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());

        // Add CartActivity's layout to BaseActivity's FrameLayout
        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());// Corrected to use binding's root

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        managmentCart = new ManagmentCart(this); // Initialize managementCart
        setVariable();
        initlist();
        calculatorCart();
        statusBarColor();
        buttonNavigation();
        loadUserAddress();
        setupPaymentMethodDropdown();
    }

    private void statusBarColor() {
        Window window = CartActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CartActivity.this, R.color.dark_green));
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, MainActivity.class)));
        binding.OrderNowBtn.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, ConfirmActivity.class));
            initlist();
        });
    }

    private void initlist() {
        ArrayList<ItemsModel> cartItems = managmentCart.getListCart();
        if (cartItems == null || cartItems.isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scroll.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scroll.setVisibility(View.VISIBLE);

            binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            CartAdapter adapter = new CartAdapter(cartItems, managmentCart, this::calculatorCart);
            binding.cartView.setAdapter(adapter);
        }
    }

    private void calculatorCart(){
        double percentTax = 0.02; // 2% tax
        double delivery = 10; // Flat delivery fee
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;

        binding.totalFeeTxt.setText("Tk " + itemTotal);
        binding.taxTxt.setText("Tk " + tax);
        if(itemTotal != 0)
            binding.deliveryTxt.setText("Tk " + delivery);
        binding.totalTxt.setText("Tk " + total);
    }

    private void setVariable(){
        binding.backBtn.setVisibility(View.VISIBLE);
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void loadUserAddress() {
        String userId = fAuth.getCurrentUser().getUid(); // Get the current user's ID

        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Fetch and display the user's address
                String address = documentSnapshot.getString("UserAddress"); // Ensure Firestore has an 'address' field
                binding.addressTxt.setText(address != null ? address : "Address not available");
            } else {
                binding.addressTxt.setText("Address not found");
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(CartActivity.this, "Failed to load address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupPaymentMethodDropdown() {
        // Define payment methods
        String[] paymentMethods = {"Bkash", "Nagad", "Bank Transfer", "Cash on Delivery"};

        // Create an ArrayAdapter for the dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                paymentMethods
        );

        // Attach the adapter to the AutoCompleteTextView
        AutoCompleteTextView paymentDropdown = binding.paymentMethodDropdown;
        paymentDropdown.setAdapter(adapter);

        // Set a listener to capture the selected item
        paymentDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedMethod = (String) parent.getItemAtPosition(position);

            Intent intent = new Intent(CartActivity.this, ConfirmActivity.class);
            intent.putExtra("selectedPaymentMethod", selectedMethod); // Pass selected method
            startActivity(intent);
        });
    }

    private void updateMoneyStatus(String status) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Users").whereEqualTo("isCustomer", "1") .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                document.getReference().update("moneyStatus", status)
                        .addOnSuccessListener(aVoid -> Log.d("CartActivity", "Status updated"))
                        .addOnFailureListener(e -> Log.e("CartActivity", "Error updating status", e));
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error fetching users for status update.", Toast.LENGTH_SHORT).show();
        });
    }


}