package com.example.groceryshop01.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.groceryshop01.Adapter.ItemsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public LiveData<List<ItemsModel>> loadFiltered(int categoryId) {
        MutableLiveData<List<ItemsModel>> listData = new MutableLiveData<>();
        Query query = firebaseDatabase.getReference("items")
                .orderByChild("categoryId")
                .equalTo(String.valueOf(categoryId));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ItemsModel> itemsList = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        itemsList.add(item);
                    }
                }
                listData.setValue(itemsList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log the error if needed
            }
        });

        return listData;
    }
}
