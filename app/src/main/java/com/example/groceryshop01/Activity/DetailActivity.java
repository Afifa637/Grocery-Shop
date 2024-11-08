package com.example.groceryshop01.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Domain.BestDealsDomain;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private BestDealsDomain object;
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
        object = (BestDealsDomain) getIntent().getSerializableExtra("object");

        int drawableResourseId = this.getResources().getIdentifier(object.getImagePath(), "drawable", this.getPackageName());
        Glide.with(this)
                .load(drawableResourseId)
                .into(binding.itemPic);

        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("Tk"+object.getPrice());
        binding.descriptionTxt.setText(object.getDescription());
        binding.ratingTxt.setText(object.getScore() + "");
        binding.ratingBar.setRating((float) object.getScore());


        binding.addtocartBtn.setOnClickListener(v -> {
            object.setNumberInCart(numberOrder);
            managmentCart.insertFood(object);
        });

        binding.backBtn.setOnClickListener(v -> finish());
    }
}