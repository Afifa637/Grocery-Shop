package com.example.groceryshop01.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityOrderReceivedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderReceivedActivity extends BaseActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private ManagmentCart managmentCart;
    private ActivityOrderReceivedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderReceivedBinding.inflate(getLayoutInflater());
        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        managmentCart = new ManagmentCart(this);

        buttonNavigation();
    }

    private void buttonNavigation() {
        binding.payBillBtn.setOnClickListener(v -> {
            updateMoneyStatus("received");
            Toast.makeText(this, "Payment has been received!", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateMoneyStatus(String status) {
        String orderId = getIntent().getStringExtra("orderId");
        if (orderId != null) {
            DatabaseReference orderRef = firebaseDatabase.getReference("Orders").child(orderId);

            orderRef.child("moneyStatus").setValue(status)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("OrderReceivedActivity", "Money status updated");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("OrderReceivedActivity", "Error updating money status", e);
                        Toast.makeText(this, "Error updating payment status.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("OrderReceivedActivity", "Order ID is null");
            Toast.makeText(this, "Error: User is not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }
}
