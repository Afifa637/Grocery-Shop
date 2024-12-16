package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.Adapter.DeliveryAdapter;
import com.example.groceryshop01.Domain.Order;
import com.example.groceryshop01.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OrderDispatchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeliveryAdapter adapter;
    private ImageView BackBtn;
    private List<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dispatch);

        recyclerView = findViewById(R.id.deliveryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeliveryAdapter(orders, this);
        recyclerView.setAdapter(adapter);
        BackBtn = findViewById(R.id.backBtn);


        fetchAcceptedOrders();
        statusBarColor();
        setupButtonNavigation();
    }

    private void fetchAcceptedOrders() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Orders");
        databaseRef.orderByChild("status").equalTo("accepted").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String orderId = orderSnapshot.getKey();
                    String customerName = orderSnapshot.child("customerName").getValue(String.class);
                    String moneyStatus = orderSnapshot.child("moneyStatus").getValue(String.class);

                    orders.add(new Order(customerName, orderId, moneyStatus));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Failed to fetch data", error.toException());
            }
        });
    }

    private void statusBarColor() {
        Window window = OrderDispatchActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(OrderDispatchActivity.this, R.color.dark_green));
    }

    private void setupButtonNavigation() {
        BackBtn.setOnClickListener(v -> {
            startActivity(new Intent(OrderDispatchActivity.this, AdminMainActivity.class));
        });
    }

}
