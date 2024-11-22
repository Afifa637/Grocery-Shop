package com.example.groceryshop01.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Adapter.ListItemAdapter;
import com.example.groceryshop01.R;
import com.example.groceryshop01.ViewModel.MainViewModel;
import com.example.groceryshop01.databinding.ActivityListBinding;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListActivity extends BaseActivity {

    private ActivityListBinding binding;
    private MainViewModel viewModel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        context = ListActivity.this;
        FrameLayout activityContent = findViewById(R.id.activityContent);
        setContentView(binding.getRoot());
        // Initializing ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ArrayList<ItemsModel> itemsList = new ArrayList<>();
        ListItemAdapter adapter = new ListItemAdapter(itemsList, this);
        // Set title from Intent extra
        binding.titleTxt.setText(getIntent().getStringExtra("title"));

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
        // Set up RecyclerView layout manager and adapter
        binding.view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.view.setAdapter(adapter);
        // Display progress bar while loading data
        binding.progressBar2.setVisibility(View.VISIBLE);

        // Load items and set adapter
        viewModel.loadFiltered(context, categoryId).observe(this, items -> {
            if (items.isEmpty()) {
                binding.emptyTxt.setVisibility(View.VISIBLE);
            } else {
                binding.emptyTxt.setVisibility(View.GONE);
                itemsList.clear(); // Clear the list to prevent duplicates
                itemsList.addAll(items); // Add loaded items
                adapter.notifyDataSetChanged();
                binding.view.setAdapter(new ListItemAdapter((ArrayList<ItemsModel>) items, context));
            }
            binding.progressBar2.setVisibility(View.GONE);
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("newItem")) {
            ItemsModel newItem = (ItemsModel) intent.getSerializableExtra("newItem");
            Log.d("ListActivity", "New item received: " + newItem);
            if (newItem != null && newItem.getCategoryId() == categoryId) {
                itemsList.add(newItem); // Add the new item to the list
                adapter.notifyDataSetChanged(); // Notify adapter of data change
                binding.emptyTxt.setVisibility(View.GONE); // Hide empty text if an item is added
            }
        }

        // Menu button onClick listener to navigate back to MainActivity
        binding.menuBtn.setOnClickListener(view -> {
            Intent intentToMain = new Intent(ListActivity.this, MainActivity.class);
            startActivity(intentToMain);
        });
    }

    private JSONArray readJsonFromFile(Context context, String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            InputStream is;
            if (file.exists()) {
                is = new FileInputStream(file); // Load from internal storage
            } else {
                is = context.getAssets().open(fileName); // Load from assets
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
            return new JSONArray(jsonString.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray(); // Return an empty array if any error occurs
        }
    }


}
