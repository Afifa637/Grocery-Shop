package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityConfirmBinding;
import com.example.groceryshop01.databinding.ActivityOrderReceivedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class OrderReceivedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private ManagmentCart managmentCart;
    private ActivityOrderReceivedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderReceivedBinding.inflate(getLayoutInflater());
        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        managmentCart = new ManagmentCart(this);

        buttonNavigation();
    }

    private void buttonNavigation() {
        binding.payBillBtn.setOnClickListener(v -> {
            updateMoneyStatus("received");
            Toast.makeText(this, "Payment has been received!", Toast.LENGTH_SHORT).show();
            // Redirect to the Order Dispatch Activity after updating status
            startActivity(new Intent(OrderReceivedActivity.this, OrderDispatchActivity.class));
        });
    }

    // Update the money status of the currently authenticated user
    private void updateMoneyStatus(String status) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId != null) {
            firestore.collection("Users").document(userId)
                    .update("moneyStatus", status)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("OrderReceivedActivity", "Status updated");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("OrderReceivedActivity", "Error updating status", e);
                        Toast.makeText(this, "Error updating payment status.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("OrderReceivedActivity", "User ID is null");
            Toast.makeText(this, "Error: User is not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }
}
