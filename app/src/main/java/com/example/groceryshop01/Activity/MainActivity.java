package com.example.groceryshop01.Activity;

import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.groceryshop01.Adapter.BestDealsAdapter;
import com.example.groceryshop01.Domain.BestDealsDomain;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        statusBarColor();
        initRecyclerView();
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.medium_sea_green));
    }

    private void initRecyclerView(){
        ArrayList<BestDealsDomain> items = new ArrayList<>();
        items.add(new BestDealsDomain("Orange", "orange", 400, 3, "Fresh and juicy oranges packed with vitamin C."));
        items.add(new BestDealsDomain("Apple", "apple", 500, 5, "Crisp and sweet apples, perfect for a healthy snack."));
        items.add(new BestDealsDomain("Berry", "berry", 350, 4, "A mix of berries rich in antioxidants and flavor."));
        items.add(new BestDealsDomain("Strawberry", "strawberry", 450, 2, "Delicious strawberries that are sweet and juicy."));
        items.add(new BestDealsDomain("Watermelon", "watermelon", 600, 7, "Refreshing watermelon, perfect for hot days."));

        binding.bestdealsview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.bestdealsview.setAdapter(new BestDealsAdapter(this, items));
    }
}