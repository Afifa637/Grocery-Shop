package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Adapter.ImageSelectionAdapter;
import com.example.groceryshop01.R;

import com.example.groceryshop01.databinding.ActivityAddItemBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {
    private ActivityAddItemBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Uri imageUri;
    private List<String> imageUrls = new ArrayList<>();
    private List<String> foodCategories;
    private FirebaseDatabase database;

    private static final String GITHUB_API_URL = "https://api.github.com/repos/Afifa637/Grocery-Shop/contents/images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        if (selectedImage != null) {
                            imageUri = selectedImage;
                            loadImageWithGlide(selectedImage);
                        }
                    }
                });

        fetchImageUrlsFromGitHub();
        binding.selectImg.setOnClickListener(v -> showImageSelectionDialog());
        setupFoodCategorySpinner();
        setupButtonListeners();
    }

    private void setupFoodCategorySpinner() {
        foodCategories = Arrays.asList("Select Category", "Vegetables", "Fruits", "Proteins", "Dairy", "Grains");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.foodCatSpinner.setAdapter(adapter);
    }

    private void fetchImageUrlsFromGitHub() {
        new Thread(() -> {
            try {
                URL url = new URL(GITHUB_API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject fileObject = jsonArray.getJSONObject(i);
                    String downloadUrl = fileObject.getString("download_url");
                    if (downloadUrl.endsWith(".jpg") || downloadUrl.endsWith(".png")) {
                        imageUrls.add(downloadUrl);
                    }
                }

                runOnUiThread(() -> Toast.makeText(this, "Images loaded successfully!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Failed to load images: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void showImageSelectionDialog() {
        if (imageUrls.isEmpty()) {
            Toast.makeText(this, "No images available!", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Image");

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new ImageSelectionAdapter(imageUrls, this::onImageSelected));

        builder.setView(recyclerView);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void onImageSelected(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.add_img)
                .error(R.drawable.add_img)
                .into(binding.selectedImg);
        imageUri = Uri.parse(imageUrl);
    }

    private void loadImageWithGlide(Uri uri) {
        Glide.with(this)
                .load(uri)
                .override(800, 600)
                .placeholder(R.drawable.add_img)
                .error(R.drawable.add_img)
                .into(binding.selectedImg);
    }

    private void setupButtonListeners() {
        binding.additemBtn.setOnClickListener(view -> {
            binding.additemBtn.setEnabled(false);
            String title = binding.foodName.getText().toString().trim();
            String description = binding.descriptionTxt.getText().toString().trim();
            String selectedCategory = (String) binding.foodCatSpinner.getSelectedItem();
            String priceStr = binding.foodPrice.getText().toString().trim();
            double price = priceStr.isEmpty() ? 0 : Double.parseDouble(priceStr);


            if (title.isEmpty() || description.isEmpty() || selectedCategory.equals("Select Category") || imageUri == null || priceStr.isEmpty()) {
                Toast.makeText(this, "Fill all the details.", Toast.LENGTH_SHORT).show();
                binding.additemBtn.setEnabled(true);
                return;
            }

            String categoryKey = getCategoryKey(selectedCategory);
            if (categoryKey != null) {
                uploadData(categoryKey, title, description, price, imageUri.toString());
            } else {
                Toast.makeText(this, "Invalid category selected.", Toast.LENGTH_SHORT).show();
                binding.additemBtn.setEnabled(true);
            }
        });

        binding.backBtn.setOnClickListener(v -> finish());
        statusBarColor();
    }

    private void statusBarColor() {
        Window window = AddItemActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AddItemActivity.this, R.color.dark_green));
    }

    private void uploadData(String categoryKey, String title, String description, double price, String imageUrl) {

        Map<String, Object> newItem = new HashMap<>();
        newItem.put("name", title);
        newItem.put("description", description);
        newItem.put("price", price);
        newItem.put("image", imageUrl);
        newItem.put("quantity", 1);
        newItem.put("score", 3);

        DatabaseReference categoryRef = database.getReference("categories").child(categoryKey);

        categoryRef.push().setValue(newItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Item added successfully.", Toast.LENGTH_SHORT).show();
                    binding.additemBtn.setEnabled(true);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.additemBtn.setEnabled(true);
                });
    }

    private String getCategoryKey(String selectedCategory) {
        switch (selectedCategory) {
            case "Vegetables":
                return "vegetables";
            case "Fruits":
                return "fruits";
            case "Proteins":
                return "proteins";
            case "Dairy":
                return "dairy";
            case "Grains":
                return "grains";
            default:
                return null;
        }
    }
}
