package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.groceryshop01.Adapter.DeliveryAdapter;
import com.example.groceryshop01.databinding.ActivityOrderDispatchBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OrderDispatchActivity extends AppCompatActivity {

    private ActivityOrderDispatchBinding binding;
    private FirebaseFirestore firestore;
    private ArrayList<String> customerNames = new ArrayList<>();
    private ArrayList<String> moneyStatus = new ArrayList<>();
    private DeliveryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderDispatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();

        adapter = new DeliveryAdapter(customerNames, moneyStatus);
        binding.deliveryRecyclerView.setAdapter(adapter);
        binding.deliveryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < customerNames.size(); i++) {
            Log.d("OrderDispatchActivity", "Customer: " + customerNames.get(i) + ", Status: " + moneyStatus.get(i));
        }
        if (customerNames.isEmpty() && moneyStatus.isEmpty()) {
            fetchUsers(); // This prevents calling it multiple times if the lists are already populated
        }
        buttonNavigation();
    }

    private void fetchUsers() {
        firestore.collection("Users").whereEqualTo("isCustomer", "1") .get().addOnSuccessListener(queryDocumentSnapshots -> {
            customerNames.clear();
            moneyStatus.clear();

            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String name = document.getString("FullName");
                String status = document.getString("moneyStatus"); // Ensure this field exists.

                customerNames.add(name);
                moneyStatus.add(status != null ? status : "not received");
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(OrderDispatchActivity.this, "Failed to fetch users.", Toast.LENGTH_SHORT).show());
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(OrderDispatchActivity.this, AdminMainActivity.class)));
    }
}
