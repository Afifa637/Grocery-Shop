package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.groceryshop01.Adapter.BestDealsAdapter;
import com.example.groceryshop01.Domain.BestDealsDomain;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userNameTextView = findViewById(R.id.userName);

        statusBarColor();
        initRecyclerView();
        bottomNavigation();
        loadUserName();
        setVariable();
    }

    private void setVariable() {
        binding.vegImg.setOnClickListener(v -> startListActivity(1, "Vegetables"));
        binding.fruitImg.setOnClickListener(v -> startListActivity(2, "Fruits"));
        binding.protImg.setOnClickListener(v -> startListActivity(3, "Proteins"));
        binding.dairyImg.setOnClickListener(v -> startListActivity(4, "Dairy"));
        binding.breadImg.setOnClickListener(v -> startListActivity(5, "Breads"));
    }

    private void loadUserName() {
        String userId = fAuth.getCurrentUser().getUid();
        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String fullName = documentSnapshot.getString("FullName");
                userNameTextView.setText(fullName);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(MainActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
        });
    }

    private void startListActivity(int id, String title) {
        Intent intent = new Intent(this, ListActivity.class);  // Fixed 'val' to 'Intent intent'
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private void bottomNavigation() {
        binding.homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Login.class)));
        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        binding.profBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.medium_sea_green));
    }

    private void initRecyclerView() {
        ArrayList<BestDealsDomain> items = new ArrayList<>();
        items.add(new BestDealsDomain("Orange", "orange", 400, 3, "Fresh and juicy oranges packed with vitamin C."));
        items.add(new BestDealsDomain("Apple", "apple", 500, 5, "Crisp and sweet apples, perfect for a healthy snack."));
        items.add(new BestDealsDomain("Berry", "berry", 350, 4, "A mix of berries rich in antioxidants and flavor."));
        items.add(new BestDealsDomain("Strawberry", "strawberry", 450, 2, "Delicious strawberries that are sweet and juicy."));
        items.add(new BestDealsDomain("Watermelon", "watermelon", 600, 7, "Refreshing watermelon, perfect for hot days."));

        binding.bestdealsview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.bestdealsview.setAdapter(new BestDealsAdapter(items));
    }
}
