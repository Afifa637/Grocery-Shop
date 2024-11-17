package com.example.groceryshop01.Activity;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Domain.BestDealsDomain;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private BestDealsDomain bestDealsDomain;
    private ItemsModel itemsModel;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getBundles();
        managmentCart = new ManagmentCart(this);
        statusBarColor();
    }

    private void statusBarColor() {
        Window window = DetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(DetailActivity.this, R.color.whit2));
    }


    private void getBundles(){
        // Check if the intent contains "BestDealsDomain"
        bestDealsDomain = (BestDealsDomain) getIntent().getSerializableExtra("bestDealsDomain");
        itemsModel = (ItemsModel) getIntent().getSerializableExtra("itemsModel");

        // Determine which object to use, prioritize BestDealsDomain if both exist
        if (bestDealsDomain != null) {
            // Handle BestDealsDomain
            setUpUIForBestDealsDomain(bestDealsDomain);
        } else if (itemsModel != null) {
            setUpUIForItemsModel(itemsModel);
        }

        // Add to cart functionality
        binding.addtocartBtn.setOnClickListener(v -> {
            if(bestDealsDomain != null)
            bestDealsDomain.setCategoryId(numberOrder); // Set the number of orders or any other property
            if (itemsModel != null) {
                //managmentCart.insertFood(itemsModel);  // Add ItemsModel to cart
            } else if (bestDealsDomain != null) {
                managmentCart.insertFood(bestDealsDomain);  // Add BestDealsDomain to cart
            }
            // Add the item to cart
        });

        binding.backBtn.setOnClickListener(v -> finish());  // Back button action
    }

    private void setUpUIForBestDealsDomain(BestDealsDomain domain) {
        int drawableResourseId = this.getResources().getIdentifier(domain.getImagePath(), "drawable", this.getPackageName());
        Glide.with(this)
                .load(drawableResourseId)  // Assuming the image path is in the form of a drawable resource name
                .into(binding.itemPic);

        binding.titleTxt.setText(domain.getTitle());
        binding.priceTxt.setText("Tk" + domain.getPrice());
        binding.descriptionTxt.setText(domain.getDescription());
        binding.ratingTxt.setText(String.valueOf(domain.getScore()));
        binding.ratingBar.setRating((float) domain.getScore());
    }

    private void setUpUIForItemsModel(ItemsModel item) {
        Glide.with(this)
                .load(item.getPicUrl())  // Assuming picUrl is a URL or resource string
                .into(binding.itemPic);

        binding.titleTxt.setText(item.getTitle());
        binding.priceTxt.setText("Tk" + item.getPrice());
        binding.descriptionTxt.setText(item.getDescription());
        binding.ratingTxt.setText(String.valueOf(item.getScore()));
        binding.ratingBar.setRating((float) item.getScore());
    }
}