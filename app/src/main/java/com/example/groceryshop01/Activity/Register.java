package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.groceryshop01.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText fullname, email, password, address;
    Button registerBtn, gotoLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isAdminBox, isCustomerBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullname = findViewById(R.id.regName);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        address = findViewById(R.id.addressTxt);
        registerBtn = findViewById(R.id.buttonCreateAccount);
        gotoLogin = findViewById(R.id.buttonLogin);
        isAdminBox = findViewById(R.id.checkBoxAdmin);
        isCustomerBox = findViewById(R.id.checkBoxCustomer);

        isCustomerBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                isAdminBox.setChecked(false);
            }
        });

        isAdminBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                isCustomerBox.setChecked(false);
            }
        });

        registerBtn.setOnClickListener(v -> {
            valid = true;

            checkField(fullname);
            checkField(email);
            checkField(password);
            checkField(address);

            if (!(isAdminBox.isChecked() || isCustomerBox.isChecked())) {
                Toast.makeText(Register.this, "Select the Account Type", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (valid) {
                fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(authResult -> {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();

                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName", fullname.getText().toString());
                            userInfo.put("UserEmail", email.getText().toString());
                            userInfo.put("UserAddress", address.getText().toString());
                            userInfo.put("moneyStatus", "not received");

                            if (isAdminBox.isChecked()) {
                                userInfo.put("isAdmin", "1");
                            }
                            if (isCustomerBox.isChecked()) {
                                userInfo.put("isCustomer", "1");
                            }

                            df.set(userInfo);

                            if (isAdminBox.isChecked()) {
                                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                            } else if (isCustomerBox.isChecked()) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Register.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        });
            }
        });

        gotoLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
        statusBarColor();
    }

    private void statusBarColor() {
        Window window = Register.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Register.this, R.color.dark_green));
    }

    public void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
    }
}