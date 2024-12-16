package com.example.groceryshop01.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityDetailBinding;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private ItemsModel itemsModel;
    private Context context;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        managmentCart = new ManagmentCart(this);

        getBundles();
        statusBarColor();
    }

    private void statusBarColor() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_green));
    }

    private void getBundles() {
        itemsModel = (ItemsModel) getIntent().getParcelableExtra("itemsModel");
        float scrTxt = getIntent().getFloatExtra("scrTxt", 0.0f); // Retrieve scrTxt value

        if (itemsModel != null) {
            setUpUIForItemsModel(itemsModel, scrTxt);
        }

        binding.addtocartBtn.setOnClickListener(v -> {
            if (itemsModel != null) {
                managmentCart.insertFood(itemsModel);
            }
        });

        binding.backBtn.setOnClickListener(v -> finish());
        binding.cartView.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
    }

    private void setUpUIForItemsModel(ItemsModel item, float scrTxt) {

        Glide.with(this)
                .load(item.getImage())
                .placeholder(R.drawable.add_img)
                .into(binding.itemPic);

        binding.titleTxt.setText(item.getName());
        binding.priceTxt.setText(String.format(Locale.getDefault(), "%.2f BDT", item.getPrice()));
        binding.descriptionTxt.setText(item.getDescription());
    }

    private void updateItemRatingLocally(float inputScore) {
        if (itemsModel != null) {
            itemsModel.setScore(inputScore);
            Toast.makeText(this, "Rating updated locally!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update rating.", Toast.LENGTH_SHORT).show();
        }
    }
}
