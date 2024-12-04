package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityAdminMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        com.example.groceryshop01.databinding.ActivityAdminMainBinding binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listenToRevenueUpdates();
        // Set up the click listener for the addMenu button
        binding.addMenu.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        binding.orderDisBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, OrderDispatchActivity.class);
            startActivity(intent);
        });

        binding.logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, Login.class);
            startActivity(intent);
        });

        binding.allItem.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, AllItemActivity.class);
            startActivity(intent);
        });

        binding.pendingOrder.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, PendingOrderActivity.class);
            startActivity(intent);
        });

        binding.profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, AdminProfileActivity.class);
            startActivity(intent);
        });
        statusBarColor();
    }

    private void statusBarColor() {
        Window window = AdminMainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AdminMainActivity.this, R.color.dark_green));
    }

    private void listenToRevenueUpdates() {
        DatabaseReference revenueRef = FirebaseDatabase.getInstance().getReference("Admin/revenue");

        TextView revenueTxt = findViewById(R.id.totalRevenueTxt);
        TextView completedTxt = findViewById(R.id.completedTxt);

        if (revenueTxt == null || completedTxt == null) {
            Log.e("AdminMainActivity", "TextViews not found in layout");
            return;
        }

        revenueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Double totalRevenue = dataSnapshot.child("totalRevenue").getValue(Double.class);
                    Long completedOrders = dataSnapshot.child("completedOrders").getValue(Long.class);

                    if (totalRevenue != null) {
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                        revenueTxt.setText(decimalFormat.format(totalRevenue) + " Tk");
                    } else {
                        revenueTxt.setText("0 Tk");
                    }

                    if (completedOrders != null) {
                        completedTxt.setText(String.valueOf(completedOrders));
                    } else {
                        completedTxt.setText("0");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("AdminMainActivity", "Failed to read revenue data", databaseError.toException());
            }
        });
    }
}
