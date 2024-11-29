package com.example.groceryshop01.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Adapter.ListItemAdapter;
import com.example.groceryshop01.R;
import com.example.groceryshop01.ViewModel.MainViewModel;
import com.example.groceryshop01.databinding.ActivityListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends BaseActivity {

    private ActivityListBinding binding;
    private MainViewModel viewModel;
    private Context context;
    private ArrayList<ItemsModel> itemsList = new ArrayList<>();
    private ListItemAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        context = ListActivity.this;

        FrameLayout activityContent = findViewById(R.id.activityContent);
        activityContent.addView(binding.getRoot());

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initializing ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Set title from Intent extra
        binding.titleTxt.setText(getIntent().getStringExtra("title"));

        // Set up RecyclerView and Adapter
        adapter = new ListItemAdapter(itemsList, this);
        binding.view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.view.setAdapter(adapter);

        // Show progress bar while fetching data
        binding.progressBar2.setVisibility(View.VISIBLE);

        // Get the category ID from Intent extra
        int categoryId = getIntent().getIntExtra("id", -1);
        Log.d("ListActivity", "Category ID received: " + categoryId);

        // Set category image based on ID
        switch (categoryId) {
            case 1:
                binding.pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vegetables));
                break;
            case 2:
                binding.pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fruits));
                break;
            case 3:
                binding.pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.proteins));
                break;
            case 4:
                binding.pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dairy));
                break;
            default:
                binding.pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.grocery_bg));
                break;
        }

        // Fetch items based on category ID
        fetchItemsFromFirebase(categoryId);

        // Menu button onClick listener to navigate back to MainActivity
        binding.menuBtn.setOnClickListener(view -> {
            Intent intentToMain = new Intent(ListActivity.this, MainActivity.class);
            startActivity(intentToMain);
        });
    }

    // Fetch items from Firebase based on the category ID
    private void fetchItemsFromFirebase(int categoryId) {
        binding.progressBar2.setVisibility(View.VISIBLE);

        // Map the categoryId to the Firebase category name
        String categoryName = getCategoryNameById(categoryId);

        if (categoryName != null) {
            databaseReference.child("categories").child(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemsList.clear();  // Clear the list to avoid duplicates

                    if (!snapshot.exists()) {
                        Log.d("ListActivity", "No items found for category: " + categoryName);
                    }

                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        ItemsModel item = itemSnapshot.getValue(ItemsModel.class);
                        if (item != null) {
                            Log.d("ListActivity", "Item: " + item.getName());
                            itemsList.add(item);  // Add the item to the list
                        }
                    }

                    if (itemsList.isEmpty()) {
                        binding.emptyTxt.setVisibility(View.VISIBLE);
                        Log.d("ListActivity", "No items to display.");
                    } else {
                        binding.emptyTxt.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();  // Notify adapter to refresh the view
                    }

                    binding.progressBar2.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBar2.setVisibility(View.GONE);
                    Toast.makeText(ListActivity.this, "Failed to load items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Map categoryId to Firebase category name
    private String getCategoryNameById(int categoryId) {
        switch (categoryId) {
            case 1:
                return "vegetables";
            case 2:
                return "fruits";
            case 3:
                return "proteins";
            case 4:
                return "dairy";
            default:
                return null;
        }
    }

}
