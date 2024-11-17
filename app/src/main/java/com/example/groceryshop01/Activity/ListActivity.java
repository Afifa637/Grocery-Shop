package com.example.groceryshop01.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Adapter.ListItemAdapter;
import com.example.groceryshop01.R;
import com.example.groceryshop01.ViewModel.MainViewModel;
import com.example.groceryshop01.databinding.ActivityListBinding;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ActivityListBinding binding;
    private MainViewModel viewModel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        context = ListActivity.this;
        setContentView(binding.getRoot());

        // Initializing ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Set title from Intent extra
        binding.titleTxt.setText(getIntent().getStringExtra("title"));

        // Get the category ID from Intent extra
        int categoryId = getIntent().getIntExtra("id", 0);

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

        // Display progress bar while loading data
        binding.progressBar2.setVisibility(View.VISIBLE);

        // Load items and set adapter
        viewModel.loadFiltered(context, categoryId).observe(this, items -> {
            if (items.isEmpty()) {
                binding.emptyTxt.setVisibility(View.VISIBLE);
            } else {
                binding.emptyTxt.setVisibility(View.GONE);
                binding.view.setAdapter(new ListItemAdapter((ArrayList<ItemsModel>) items, context));
            }
            binding.progressBar2.setVisibility(View.GONE);
        });

        // Menu button onClick listener to navigate back to MainActivity
        binding.menuBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
