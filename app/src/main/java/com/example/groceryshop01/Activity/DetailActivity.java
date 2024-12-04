package com.example.groceryshop01.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityDetailBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private ItemsModel itemsModel;
    private Context context;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        getBundles();
        managmentCart = new ManagmentCart(this);
        statusBarColor();
    }

    private void statusBarColor() {
        Window window = DetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(DetailActivity.this, R.color.dark_green));
    }

    private void getBundles() {
        itemsModel = (ItemsModel) getIntent().getSerializableExtra("itemsModel");
        Log.d("ReceivedItem", itemsModel != null ? itemsModel.toString() : "No data received");

        if (itemsModel != null) {
            setUpUIForItemsModel(itemsModel);
        }

        // Add to cart functionality
        binding.addtocartBtn.setOnClickListener(v -> {
            if (itemsModel != null) {
                managmentCart.insertFood(itemsModel);
            }
        });

        binding.ratingBtn.setOnClickListener(v -> showRatingMenu());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.cartView.setOnClickListener(v -> startActivity(new Intent(DetailActivity.this, CartActivity.class)));
    }

    private void showRatingMenu() {
        PopupMenu popupMenu = new PopupMenu(context, binding.ratingBtn);
        Menu menu = popupMenu.getMenu();

        // Add rating options (1 to 5 stars)
        for (int i = 1; i <= 5; i++) {
            menu.add(Menu.NONE, i, i, i + " Stars");
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            int selectedRating = item.getItemId(); // Get selected rating (1 to 5)
            itemsModel.setScore(selectedRating);  // Set the selected rating for the item
            updateItemRating(itemsModel);  // Update the rating in Firebase
            return true;
        });

        popupMenu.show();
    }

    private void updateItemRating(ItemsModel item) {
        // Ensure the item has a valid ID before trying to update its score
        if (item != null && item.getItemKey() != null) { // Ensure itemKey is not null
            // Dynamically select the category and item key
            DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("categories")
                    .child(item.getCategoryKey())  // Dynamically choose category based on the item
                    .child(item.getItemKey());  // Use itemKey to find the item in the database

            // Update the score in the database
            itemRef.child("score").setValue(item.getScore())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("UpdateRating", "Item rating updated successfully.");
                        } else {
                            Log.e("UpdateRating", "Failed to update item rating.", task.getException());
                        }
                    });
        } else {
            Log.e("UpdateRating", "Item or Item ID is null, cannot update rating.");
        }
    }

    private void setUpUIForItemsModel(ItemsModel item) {
        Glide.with(this)
                .load(item.getImage())  // Assuming picUrl is a URL or resource string
                .into(binding.itemPic);

        binding.titleTxt.setText(item.getName());
        binding.priceTxt.setText(item.getPrice() + " BDT");
        binding.descriptionTxt.setText(item.getDescription());
        binding.ratingTxt.setText(String.valueOf(item.getScore()));
        binding.ratingBar.setRating((float) item.getScore());
    }
}
