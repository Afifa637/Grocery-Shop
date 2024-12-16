package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.groceryshop01.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edtForgotPasswordEmail;
    AppCompatButton btnReset, btnForgotPasswordBack;
    ProgressBar forgetPasswordProgressbar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fAuth = FirebaseAuth.getInstance();

        edtForgotPasswordEmail = findViewById(R.id.edtForgotPasswordEmail);
        btnReset = findViewById(R.id.btnReset);
        btnForgotPasswordBack = findViewById(R.id.btnForgotPasswordBack);
        forgetPasswordProgressbar = findViewById(R.id.forgetPasswordProgressbar);

        btnReset.setOnClickListener(v -> {
            String email = edtForgotPasswordEmail.getText().toString().trim();

            if (email.isEmpty()) {
                edtForgotPasswordEmail.setError("Email is required!");
                edtForgotPasswordEmail.requestFocus();
                return;
            }

            forgetPasswordProgressbar.setVisibility(View.VISIBLE);

            fAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                forgetPasswordProgressbar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()) {
                    Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Login.class));
                    finish();
                } else {
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnForgotPasswordBack.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });

        statusBarColor();
    }

    private void statusBarColor() {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.dark_green));
    }
}
