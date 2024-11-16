package com.example.groceryshop01.Adapter;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsModel implements Serializable {

    private String title;
    private String description;
    private ArrayList<String> picUrl;  // Assuming picUrl is a list of URLs for images
    private double price;
    private double score;
    private int categoryId;

    // Default constructor (required for Firebase)
    public ItemsModel() {}

    // Constructor with parameters
    public ItemsModel(String title, String description, ArrayList<String> picUrl, double price, double score, int categoryId) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.score = score;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


}
