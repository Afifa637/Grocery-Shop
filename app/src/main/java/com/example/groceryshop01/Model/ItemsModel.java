package com.example.groceryshop01.Adapter;

import androidx.lifecycle.MutableLiveData;
import com.example.groceryshop01.Domain.ItemsDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ItemsModel {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public MutableLiveData<ArrayList<ItemsDomain>> loadFiltered(int id) {
        MutableLiveData<ArrayList<ItemsDomain>> liveData = new MutableLiveData<>();
        Query query = firebaseDatabase.getReference("Items")
                .orderByChild("categoryId")
                .equalTo(String.valueOf(id));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<ItemsDomain> itemsList = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsDomain item = childSnapshot.getValue(ItemsDomain.class);
                    if (item != null) {
                        itemsList.add(item);
                    }
                }
                liveData.setValue(itemsList); // Set data to LiveData
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any errors
            }
        });
        return liveData;
    }
}
