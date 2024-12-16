package com.example.groceryshop01.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Repository.MainRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final MainRepository repository;

    public MainViewModel() {
        repository = new MainRepository();
    }

    public LiveData<List<ItemsModel>> loadFiltered(Context context, int categoryId) {
        return repository.loadFiltered(context, categoryId);
    }
}