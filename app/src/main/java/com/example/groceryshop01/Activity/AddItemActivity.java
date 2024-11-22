package com.example.groceryshop01.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.R;

import com.example.groceryshop01.databinding.ActivityAddItemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {
    private ActivityAddItemBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri imageUri;
    private List<String> foodCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the gallery and camera launchers
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        if (selectedImage != null) {
                            loadImageWithGlide(selectedImage);
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && imageUri != null) {
                        loadImageWithGlide(imageUri);
                    }
                });

        // Set up the button click listener to show a popup menu for image selection
        binding.pickImg.setOnClickListener(this::showImagePickerPopup);
        setupFoodCategorySpinner();
        buttonNavigation();
    }

    private void setupFoodCategorySpinner() {
        // Define the list of food categories
        foodCategories = Arrays.asList("Select Category", "Vegetables", "Fruits", "Proteins", "Dairy", "Grains", "Drinks");

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                foodCategories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        binding.foodCatSpinner.setAdapter(adapter);

        // Set a listener to capture selected category
        binding.foodCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    // Get the selected category
                    String selectedCategory = foodCategories.get(position);
                    Toast.makeText(AddItemActivity.this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });
    }

    private void showImagePickerPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.image_picker_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_gallery) {
                openGallery();
                return true;
            } else if (id == R.id.menu_camera) {
                openCamera();
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void openCamera() {
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraLauncher.launch(imageUri);
    }

    private void loadImageWithGlide(Uri uri) {
        // Use Glide to load the image into the ImageView
        Glide.with(this)
                .load(uri)
                .override(800, 600) // Resize to avoid loading large bitmaps
                .placeholder(R.drawable.add_img) // Optional placeholder while loading
                .error(R.drawable.add_img) // Optional error image
                .into(binding.selectedImg);
    }

    private void buttonNavigation() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(AddItemActivity.this, AdminMainActivity.class)));
        binding.additemBtn.setOnClickListener(view -> {
            String title = binding.foodName.getText().toString().trim();
            String description = binding.descriptionTxt.getText().toString().trim();
            String selectedCategory = (String) binding.foodCatSpinner.getSelectedItem();
            String priceStr = binding.foodPrice.getText().toString();
            double price = priceStr.isEmpty() ? 0 : Double.parseDouble(priceStr);
            String imgUri = imageUri != null ? imageUri.toString() : "";

            if (title.isEmpty() || description.isEmpty() || selectedCategory.equals("Select Category")) {
                Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
                return;
            }

            int categoryId = foodCategories.indexOf(selectedCategory);

            ItemsModel newItem = new ItemsModel(
                    title, description, imgUri, price, 0, categoryId, 0
            );

            try {
                JSONArray jsonArray = readJsonFromFile(this, "categories.json");
                JSONObject newItemJson = new JSONObject();
                newItemJson.put("type", "itemsModel");
                newItemJson.put("title", newItem.getTitle())    ;
                newItemJson.put("description", newItem.getDescription());
                newItemJson.put("picUrl", newItem.getPicUrl());
                newItemJson.put("price", newItem.getPrice());
                newItemJson.put("score", newItem.getScore());
                newItemJson.put("categoryId", newItem.getCategoryId());

                jsonArray.put(newItemJson);
                writeJsonToFile(this, "items.json", jsonArray);

                Toast.makeText(this, "Item added successfully.", Toast.LENGTH_SHORT).show();

                // Return to ListActivity with updated categoryId
                Intent intent = new Intent(AddItemActivity.this, ListActivity.class);
                intent.putExtra("id", categoryId);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error adding item.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private JSONArray readJsonFromFile(Context context, String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            InputStream is;
            if (file.exists()) {
                is = new FileInputStream(file); // Load from internal storage
            } else {
                is = context.getAssets().open(fileName); // Load from assets
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
            return new JSONArray(jsonString.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray(); // Return an empty array if any error occurs
        }
    }


    private void writeJsonToFile(Context context, String fileName, JSONArray jsonArray) {
        try {
            File file = new File(context.getFilesDir(), fileName); // Write to internal storage
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}
