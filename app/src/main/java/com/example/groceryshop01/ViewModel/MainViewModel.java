package com.example.groceryshop01.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.groceryshop01.Domain.ItemsDomain;
import com.example.groceryshop01.Adapter.ItemsModel;
import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private final ItemsModel itemsModel;

    public MainViewModel() {
        itemsModel = new ItemsModel();  // Initialize ItemsModel
    }

    public LiveData<ArrayList<ItemsDomain>> loadFiltered(int id) {
        return itemsModel.loadFiltered(id);  // Call ItemsModel method
    }
}
