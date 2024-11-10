package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.groceryshop01.Adapter.CartAdapter;
import com.example.groceryshop01.Helper.ChangeNumberItemsListener;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {

    private ManagmentCart managmentCart;
    private ActivityCartBinding binding;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Corrected to use binding's root

        managmentCart = new ManagmentCart(this); // Initialize managmentCart
        setVariable();
        initlist();
        calculatorCart();
        statusBarColor();
        buttonNavigation();
    }

    private void statusBarColor() {
        Window window = CartActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CartActivity.this, R.color.medium_sea_green));
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, MainActivity.class)));
    }

    private void initlist(){
        // Check if the cart is empty and show/hide elements accordingly
        if(managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scroll.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scroll.setVisibility(View.VISIBLE);
        }

        // Initialize RecyclerView with CartAdapter and set layout manager
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), managmentCart, () -> calculatorCart()));
    }

    private void calculatorCart(){
        double percentTax = 0.02; // 2% tax
        double delivery = 10; // Flat delivery fee
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;

        binding.totalFeeTxt.setText("Tk " + itemTotal);
        binding.taxTxt.setText("Tk " + tax);
        binding.deliveryTxt.setText("Tk " + delivery);
        binding.totalTxt.setText("Tk " + total);
    }

    private void setVariable(){
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
