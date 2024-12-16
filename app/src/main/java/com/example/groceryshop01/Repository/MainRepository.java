package com.example.groceryshop01.Repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.groceryshop01.Domain.ItemsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public LiveData<List<ItemsModel>> loadFiltered(Context context, int categoryId) {
        MutableLiveData<List<ItemsModel>> listData = new MutableLiveData<>();

        try {
            String jsonString = loadJSONFromAsset(context, "categories.json");
            if (jsonString != null) {
                Type listType = new TypeToken<ArrayList<ItemsModel>>() {}.getType();
                List<ItemsModel> allItems = new Gson().fromJson(jsonString, listType);

                List<ItemsModel> categoryItems = new ArrayList<>();
                for (ItemsModel item : allItems) {
                    if (item.getCategoryId() == categoryId) {
                        categoryItems.add(item);
                    }
                }
                listData.postValue(categoryItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load from Firebase
        Query query = firebaseDatabase.getReference("items")
                .orderByChild("categoryId")
                .equalTo(categoryId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ItemsModel> firebaseItems = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        firebaseItems.add(item);
                    }
                }

                if (!firebaseItems.isEmpty()) {
                    listData.postValue(firebaseItems);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        });

        return listData;
    }

    private String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainRepository", "Error loading JSON file: " + fileName, e);
        }
        return json;
    }
}
