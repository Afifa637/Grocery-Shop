package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.groceryshop01.Adapter.DeliveryAdapter;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityOrderDispatchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDispatchActivity extends AppCompatActivity {

    private ActivityOrderDispatchBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> customerNames = new ArrayList<>();
    private ArrayList<String> moneyStatus = new ArrayList<>();
    private ArrayList<String> orderIds = new ArrayList<>();
    private DeliveryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderDispatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();

        adapter = new DeliveryAdapter(customerNames, moneyStatus, orderIds);
        binding.deliveryRecyclerView.setAdapter(adapter);
        binding.deliveryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (customerNames.isEmpty() && moneyStatus.isEmpty()) {
            fetchUsers();
        }
        buttonNavigation();
        statusBarColor();
    }

    private void fetchUsers() {
        DatabaseReference ordersRef = firebaseDatabase.getReference("Orders");

        // Fetch orders with "accepted" status and "not received" moneyStatus
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerNames.clear();
                moneyStatus.clear();
                orderIds.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String status = orderSnapshot.child("status").getValue(String.class);
                    String moneyStatusValue = orderSnapshot.child("moneyStatus").getValue(String.class);
                    String customerName = orderSnapshot.child("customerName").getValue(String.class);
                    String orderId = orderSnapshot.child("orderId").getValue(String.class);

                    // Check for status "accepted" and moneyStatus "not received"
                    if ("accepted".equals(status) && "not received".equals(moneyStatusValue)) {
                        if (customerName != null && !customerName.isEmpty() && orderId != null) {
                            customerNames.add(customerName);
                            moneyStatus.add(moneyStatusValue);
                            orderIds.add(orderId);
                        }
                    }
                }
                if (customerNames.isEmpty()) {
                    Toast.makeText(OrderDispatchActivity.this, "No orders to dispatch.", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDispatchActivity.this, "Failed to fetch orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void statusBarColor() {
        Window window = OrderDispatchActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(OrderDispatchActivity.this, R.color.dark_green));
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v ->
                startActivity(new Intent(OrderDispatchActivity.this, AdminMainActivity.class))
        );
    }

    // Method to update the button state when accepting an order
    public void acceptOrder(String orderId, Button acceptBtn) {

        // Update the status in the database
        DatabaseReference orderRef = firebaseDatabase.getReference("Orders").child(orderId);
        orderRef.child("status").setValue("accepted")
                .addOnSuccessListener(aVoid -> {
                    Log.d("OrderDispatchActivity", "Order status updated to accepted");
                })
                .addOnFailureListener(e -> {
                    Log.e("OrderDispatchActivity", "Error updating order status", e);
                    Toast.makeText(OrderDispatchActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                });
    }
}
