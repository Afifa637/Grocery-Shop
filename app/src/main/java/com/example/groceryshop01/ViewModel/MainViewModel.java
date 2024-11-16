package com.example.groceryshop01.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.groceryshop01.Adapter.ItemsModel;
import com.example.groceryshop01.Repository.MainRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final MainRepository repository;

    public MainViewModel() {
        repository = new MainRepository();  // Initialize repository
    }

    public LiveData<List<ItemsModel>> loadFiltered(int categoryId) {
        return repository.loadFiltered(categoryId);  // Call repository method
    }
}
