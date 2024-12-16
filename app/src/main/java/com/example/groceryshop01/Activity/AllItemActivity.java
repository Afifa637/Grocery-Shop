package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.groceryshop01.Adapter.ListItemAdapter;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityAllItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllItemActivity extends AppCompatActivity {

    private ActivityAllItemBinding binding;
    private ListItemAdapter adapter;
    private ArrayList<ItemsModel> itemsList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new ListItemAdapter(itemsList, this, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.MenuRecyclerView.setLayoutManager(gridLayoutManager);
        binding.MenuRecyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("categories");

        setupCategoryClickListeners();
        statusBarColor();
        setupButtonNavigation();
    }

    private void statusBarColor() {
        Window window = AllItemActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AllItemActivity.this, R.color.dark_green));
    }

    private void setupCategoryClickListeners() {
        binding.vegImg.setOnClickListener(v -> fetchItemsByCategory("vegetables"));
        binding.fruitImg.setOnClickListener(v -> fetchItemsByCategory("fruits"));
        binding.protImg.setOnClickListener(v -> fetchItemsByCategory("proteins"));
        binding.dairyImg.setOnClickListener(v -> fetchItemsByCategory("dairy"));
        binding.breadImg.setOnClickListener(v -> fetchItemsByCategory("breads"));
    }

    private void fetchItemsByCategory(String categoryKey) {
        binding.progressBar.setVisibility(View.VISIBLE);

        databaseReference.child(categoryKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemsList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ItemsModel item = itemSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        item.setCategoryKey(categoryKey);
                        item.setItemKey(itemSnapshot.getKey());
                        itemsList.add(item);
                    }
                }

                if (itemsList.isEmpty()) {
                    Toast.makeText(AllItemActivity.this, "No items found in " + categoryKey, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AllItemActivity.this, "Failed to load items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(AllItemActivity.this, AdminMainActivity.class)));
    }

    public void showEditDialog(ItemsModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_item, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.editItemName);
        EditText priceInput = dialogView.findViewById(R.id.editItemPrice);
        EditText quantityInput = dialogView.findViewById(R.id.editItemQuantity);

        nameInput.setText(item.getName());
        priceInput.setText(String.valueOf(item.getPrice()));
        quantityInput.setText(String.valueOf(item.getQuantity()));

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = nameInput.getText().toString().trim();
            double newPrice = Double.parseDouble(priceInput.getText().toString().trim());
            int newQuantity = Integer.parseInt(quantityInput.getText().toString().trim());

            updateItemInFirebase(item, newName, newPrice, newQuantity);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateItemInFirebase(ItemsModel item, String newName, double newPrice, int newQuantity) {
        DatabaseReference itemRef = databaseReference.child(item.getCategoryKey()).child(item.getItemKey());
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newName);
        updates.put("price", newPrice);
        updates.put("quantity", newQuantity);

        itemRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Item updated successfully.", Toast.LENGTH_SHORT).show();
                    fetchItemsByCategory(item.getCategoryKey());
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void deleteItem(ItemsModel item) {
        DatabaseReference itemRef = databaseReference.child(item.getCategoryKey()).child(item.getItemKey());
        itemRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    itemsList.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Item deleted successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
