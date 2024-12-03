
package com.example.groceryshop01.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groceryshop01.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText fullname, email, password;
    Button loginBtn, gotoRegister;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        gotoRegister = findViewById(R.id.buttonGotoReg);

        loginBtn.setOnClickListener((v -> {
            checkField(email);
            checkField(password);
            Log.d("TAG", "onClick: " + email.getText().toString());

            if (valid) {
                fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(authResult -> {
                    Toast.makeText(Login.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                    checkUserAccessLevel(authResult.getUser().getUid());
                }).addOnFailureListener(e -> Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }));

        statusBarColor();
        gotoRegister.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Register.class)));
    }

    private void statusBarColor() {
        Window window = Login.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Login.this, R.color.dark_green));
    }

    private void checkUserAccessLevel(String uid){
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
            //identify the user access level
            if (documentSnapshot.getString("isAdmin") != null) {
                //user is admin
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }
            if (documentSnapshot.getString("isCustomer") != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("isAdmin") != null){
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                    }

                    if(documentSnapshot.getString("isCustomer") != null){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });
        }
    }*/
}