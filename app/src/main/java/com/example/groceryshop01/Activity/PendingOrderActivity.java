package com.example.groceryshop01.Activity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderActivity extends AppCompatActivity {

    private RecyclerView deliveryRecyclerView;
    private PendingOrderAdapter pendingOrderAdapter;
    private DatabaseReference ordersDatabase;
    private List<PendingOrderModel> pendingOrders;
    private ActivityPendingOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ordersDatabase = FirebaseDatabase.getInstance().getReference("Orders");
        deliveryRecyclerView = binding.deliveryRecyclerView;

        setupRecyclerView();
        fetchOrdersFromDatabase();
        setupUI();
    }

    private void setupRecyclerView() {
        pendingOrders = new ArrayList<>();
        pendingOrderAdapter = new PendingOrderAdapter(pendingOrders, this, (order, position) -> {
            updateOrderStatus(order.getOrderId(), "accepted");
            updateMoneyStatus(order.getOrderId(), "not received");
            Toast.makeText(this, "Order accepted. View Dispatch for updates!", Toast.LENGTH_SHORT).show();
        });

        deliveryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deliveryRecyclerView.setAdapter(pendingOrderAdapter);
    }

    private void fetchOrdersFromDatabase() {
        ordersDatabase.orderByChild("status").equalTo("pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pendingOrders.clear(); // Clear old data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PendingOrderModel order = snapshot.getValue(PendingOrderModel.class);
                    if (order != null) {
                        pendingOrders.add(order);
                    }
                }
                pendingOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setupUI() {
        binding.backBtn.setOnClickListener(v -> finish());
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_green));
    }

    private void updateOrderStatus(String orderId, String status) {
        ordersDatabase.child(orderId).child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Log.d("PendingOrderActivity", "Order status updated successfully.");
                    if ("accepted".equals(status)) {
                        updateRevenueAndCompletedOrders(orderId);
                    }
                })
                .addOnFailureListener(e -> Log.e("PendingOrderActivity", "Failed to update order status", e));
    }

    private void updateRevenueAndCompletedOrders(String orderId) {
        DatabaseReference revenueRef = FirebaseDatabase.getInstance().getReference("Admin/revenue");
        ordersDatabase.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                PendingOrderModel order = snapshot.getValue(PendingOrderModel.class);
                if (order != null) {
                    double orderTotal = order.getTotal();

                    revenueRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Double currentRevenue = mutableData.child("totalRevenue").getValue(Double.class);
                            Long completedOrders = mutableData.child("completedOrders").getValue(Long.class);

                            if (currentRevenue == null) currentRevenue = 0.0;
                            if (completedOrders == null) completedOrders = 0L;

                            mutableData.child("totalRevenue").setValue(currentRevenue + orderTotal);
                            mutableData.child("completedOrders").setValue(completedOrders + 1);

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                            if (error == null) {
                                Log.d("PendingOrderActivity", "Revenue and completed orders updated successfully.");
                            } else {
                                Log.e("PendingOrderActivity", "Failed to update revenue and completed orders", error.toException());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("PendingOrderActivity", "Failed to fetch order data for revenue update", error.toException());
            }
        });
    }


    private void updateMoneyStatus(String orderId, String status) {
        ordersDatabase.child(orderId).child("moneyStatus").setValue(status)
                .addOnSuccessListener(aVoid -> Log.d("PendingOrderActivity", "Money status updated successfully."))
                .addOnFailureListener(e -> Log.e("PendingOrderActivity", "Failed to update money status", e));
    }
}
