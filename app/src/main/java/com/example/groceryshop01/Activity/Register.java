package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryshop01.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText fullname, email, password;
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
        registerBtn = findViewById(R.id.buttonCreateAccount);
        gotoLogin = findViewById(R.id.buttonLogin);
        isAdminBox = findViewById(R.id.checkBoxAdmin);
        isCustomerBox = findViewById(R.id.checkBoxCustomer);

        //check boxes logic
        isCustomerBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isAdminBox.setChecked(false);
                }
            }
        });

        isAdminBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isCustomerBox.setChecked(false);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset valid to true at the beginning of each registration attempt
                valid = true;

                // Check each field individually
                checkField(fullname);
                checkField(email);
                checkField(password);

                // Ensure account type is selected
                if (!(isAdminBox.isChecked() || isCustomerBox.isChecked())) {
                    Toast.makeText(Register.this, "Select the Account Type", Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if (valid) {
                    // Proceed with account creation
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = fAuth.getCurrentUser();
                                    Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();

                                    // Store user data
                                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("FullName", fullname.getText().toString());
                                    userInfo.put("UserEmail", email.getText().toString());

                                    // Specify if user is admin or customer
                                    if (isAdminBox.isChecked()) {
                                        userInfo.put("isAdmin", "1");
                                    }
                                    if (isCustomerBox.isChecked()) {
                                        userInfo.put("isCustomer", "1");
                                    }

                                    // Save user information in Firestore
                                    df.set(userInfo);

                                    // Redirect to appropriate activity
                                    if (isAdminBox.isChecked()) {
                                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                    } else if (isCustomerBox.isChecked()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }
}
