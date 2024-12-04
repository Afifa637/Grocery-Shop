package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop01.Domain.DataModel;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private TextView userNameTextView;
    private Button btnFetchData;

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

        statusBarColor();
        loadUserName();
        setVariable();
    }

    private void setVariable() {
        binding.vegImg.setOnClickListener(v -> startListActivity(1, "Vegetables"));
        binding.fruitImg.setOnClickListener(v -> startListActivity(2, "Fruits"));
        binding.protImg.setOnClickListener(v -> startListActivity(3, "Proteins"));
        binding.dairyImg.setOnClickListener(v -> startListActivity(4, "Dairy"));
        binding.breadImg.setOnClickListener(v -> startListActivity(5, "Breads"));

        binding.notificationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderReceivedActivity.class);
            startActivity(intent);
        });

        binding.btnFetchData.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
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
        Intent intent = new Intent(this, ListActivity.class);  // Fixed 'val' to 'Intent intent'
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dark_green));
    }
}
