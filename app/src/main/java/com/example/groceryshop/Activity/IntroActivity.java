package com.example.groceryshop.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.groceryshop.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startBtn.setOnClickListener(v -> startActivity( new Intent(IntroActivity.this, MainActivity.class)));

    }
}