package com.example.groceryshop01.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    //private BestDealsDomain bestDealsDomain;
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

    private void getBundles(){
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

        binding.backBtn.setOnClickListener(v -> finish());
        binding.cartView.setOnClickListener(v -> startActivity(new Intent(DetailActivity.this, CartActivity.class)));
    }

    private void setUpUIForItemsModel(ItemsModel item) {
        Glide.with(this)
                .load(item.getImage())  // Assuming picUrl is a URL or resource string
                .into(binding.itemPic);

        binding.titleTxt.setText(item.getName());
        binding.priceTxt.setText("Tk" + item.getPrice());
        binding.descriptionTxt.setText(item.getDescription());
        binding.ratingTxt.setText(String.valueOf(item.getScore()));
        binding.ratingBar.setRating((float) item.getScore());
    }
}