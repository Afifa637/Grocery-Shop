package com.example.groceryshop01.Domain;

import com.example.groceryshop01.Model.CartItem;

import java.io.Serializable;

public class ItemsModel implements Serializable, CartItem {

    private String title;
    private String description;
    private String picUrl;  // Assuming picUrl is a list of URLs for images
    private double price;
    private double score;
    private int categoryId;
    private int numberInCart;

    // Default constructor (required for Firebase)
    public ItemsModel() {}

    // Constructor with parameters
    public ItemsModel(String title, String description, String picUrl, double price, double score, int categoryId, int numberInCart) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.score = score;
        this.categoryId = categoryId;
        this.numberInCart = numberInCart;
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

    @Override
    public void setImagePath(String imagePath) {

    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String getImagePath() {
        return "";
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
    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

}