package com.example.groceryshop01.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.Adapter.PendingOrderAdapter;
import com.example.groceryshop01.Domain.PendingOrderModel;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityPendingOrderBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderActivity extends AppCompatActivity {

    private RecyclerView deliveryRecyclerView;
    private PendingOrderAdapter pendingOrderAdapter;
    private FirebaseFirestore firestore;
    private List<PendingOrderModel> pendingOrders;
    private ActivityPendingOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        String customerName = sharedPreferences.getString("customerName", "Unknown");

        // Initialize Firestore and UI components
        firestore = FirebaseFirestore.getInstance();
        deliveryRecyclerView = binding.deliveryRecyclerView;

        setupRecyclerView();
        fetchOrdersFromFirestore();
        statusBarColor();
        buttonNavigation();
    }

    private void setupRecyclerView() {
        pendingOrders = new ArrayList<>();
        pendingOrderAdapter = new PendingOrderAdapter(pendingOrders, this, (order, position) -> {
            updateMoneyStatus("not received");
            Toast.makeText(this, "Order accepted. View Dispatch for updates!", Toast.LENGTH_SHORT).show();

            pendingOrders.remove(position);
            pendingOrderAdapter.notifyItemRemoved(position);
        });

        deliveryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deliveryRecyclerView.setAdapter(pendingOrderAdapter);
    }

    private void fetchOrdersFromFirestore() {
        firestore.collection("Orders")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    pendingOrders.clear();
                    if (querySnapshot.isEmpty()) {
                        Log.d("PendingOrderActivity", "No pending orders found");
                        Toast.makeText(this, "No pending orders available.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (DocumentSnapshot document : querySnapshot) {
                        PendingOrderModel order = document.toObject(PendingOrderModel.class);
                        if (order != null) {
                            Log.d("PendingOrderActivity", "Fetched order: " + order.getCustomerName());
                            if (order.getItemsList() != null) {
                                for (PendingOrderModel.Item item : order.getItemsList()) {
                                    Log.d("PendingOrderActivity", "Item: " + item.getName() + ", Quantity: " + item.getQuantity());
                                }
                            } else {
                                Log.d("PendingOrderActivity", "ItemsList is null for order: " + order.getCustomerName());
                            }
                            pendingOrders.add(order);
                        }
                    }
                    pendingOrderAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("PendingOrderActivity", "Error fetching orders", e);
                    Toast.makeText(this, "Failed to load orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });

    }

    private void statusBarColor() {
        Window window = PendingOrderActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(PendingOrderActivity.this, R.color.dark_green));
    }

    private void updateMoneyStatus(String status) {
        firestore.collection("Users")
                .whereEqualTo("isCustomer", "1")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().update("moneyStatus", status)
                                .addOnSuccessListener(aVoid -> Log.d("ConfirmActivity", "Status updated"))
                                .addOnFailureListener(e -> Log.e("ConfirmActivity", "Error updating status", e));
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching users for status update.", Toast.LENGTH_SHORT).show());
    }
}
