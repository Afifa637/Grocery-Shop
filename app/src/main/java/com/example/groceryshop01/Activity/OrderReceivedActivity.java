package com.example.groceryshop01.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.groceryshop01.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderReceivedActivity extends BaseActivity {
    private FirebaseDatabase firebaseDatabase;
    private String orderId;  // To store the orderId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_received);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // Button to handle pay bill
        Button payBillButton = findViewById(R.id.payBillBtn);

        fetchOrderIdFromDatabase();

        payBillButton.setOnClickListener(v -> {
            if (orderId != null) {
                updateMoneyStatus(orderId, "received"); // Update money status for this order
            } else {
                Log.e("OrderReceivedActivity", "Order ID is null. Cannot update moneyStatus.");
                Toast.makeText(this, "Order ID is missing!", Toast.LENGTH_SHORT).show();
            }
        });
        statusBarColor();
    }
    // Fetch the orderId from Firebase based on a condition (e.g., status is "accepted")
    private void fetchOrderIdFromDatabase() {
        DatabaseReference ordersRef = firebaseDatabase.getReference("Orders");

        ordersRef.orderByChild("status").equalTo("accepted").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        orderId = orderSnapshot.child("orderId").getValue(String.class);
                        Log.d("FirebaseDebug", "Fetched Order ID: " + orderId);  // Debugging log
                    }
                } else {
                    Log.e("OrderReceivedActivity", "No accepted orders found.");
                    Toast.makeText(OrderReceivedActivity.this, "No accepted orders found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderReceivedActivity", "Error fetching order ID", error.toException());
            }
        });
    }

    // Method to update the moneyStatus in the database
    private void updateMoneyStatus(String orderId, String status) {
        DatabaseReference orderRef = firebaseDatabase.getReference("Orders").child(orderId);

        Log.d("FirebaseDebug", "Updating moneyStatus at: " + orderRef.toString());

        orderRef.child("moneyStatus").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Log.d("OrderReceivedActivity", "Money status updated successfully for Order ID: " + orderId);
                    Toast.makeText(OrderReceivedActivity.this, "Payment received successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                })
                .addOnFailureListener(e -> {
                    Log.e("OrderReceivedActivity", "Error updating money status", e);
                    Toast.makeText(OrderReceivedActivity.this, "Error updating payment status.", Toast.LENGTH_SHORT).show();
                });
    }

    private void statusBarColor() {
        Window window = OrderReceivedActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(OrderReceivedActivity.this, R.color.dark_green));
    }
}
