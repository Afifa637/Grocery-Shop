package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.Adapter.ListItemAdapter;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private TextView userNameTextView;
    private RecyclerView recyclerViewBestDeals;
    private ListItemAdapter bestDealsAdapter;
    private ArrayList<ItemsModel> bestDealsList;
    private Button btnFetchData, seeallBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userNameTextView = findViewById(R.id.userName);

        btnFetchData = findViewById(R.id.btnFetchData);
        seeallBtn = findViewById(R.id.seeallBtn);

        statusBarColor();
        loadUserName();
        setupRecyclerView();
        setVariable();

        seeallBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra("id", 2);
            intent.putExtra("title", "Fruits");
            startActivity(intent);
        });
    }

    private void setVariable() {
        binding.vegImg.setOnClickListener(v -> startListActivity(1, "Vegetables"));
        binding.fruitImg.setOnClickListener(v -> startListActivity(2, "Fruits"));
        binding.protImg.setOnClickListener(v -> startListActivity(3, "Proteins"));
        binding.dairyImg.setOnClickListener(v -> startListActivity(4, "Dairy"));
        binding.breadImg.setOnClickListener(v -> startListActivity(5, "Breads"));

        binding.notificationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderReceivedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        binding.btnFetchData.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadUserName() {
        String userId = fAuth.getCurrentUser().getUid();
        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String fullName = documentSnapshot.getString("FullName");
                userNameTextView.setText(fullName);
            }
        }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show());
    }

    private void startListActivity(int id, String title) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        recyclerViewBestDeals = binding.recyclerViewBestDeals;
        recyclerViewBestDeals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bestDealsList = new ArrayList<>();
        bestDealsAdapter = new ListItemAdapter(bestDealsList, this, true);
        recyclerViewBestDeals.setAdapter(bestDealsAdapter);

        fetchBestDealsFromFirebase();
    }

    private void fetchBestDealsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("categories");

        databaseReference.child("vegetables").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bestDealsList.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ItemsModel item = itemSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        bestDealsList.add(item);
                    }
                }

                if (bestDealsList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No items found in this category", Toast.LENGTH_SHORT).show();
                }

                bestDealsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dark_green));
    }
}
