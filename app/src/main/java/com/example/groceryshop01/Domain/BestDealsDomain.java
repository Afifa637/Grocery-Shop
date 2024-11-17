
package com.example.groceryshop01.Domain;

import com.example.groceryshop01.Model.CartItem;

import java.io.Serializable;

public class BestDealsDomain implements Serializable , CartItem {
    private String title;
    private String imagePath;
    private double price;
    private double score;
    private String description;
    private int numberInCart;
    private int categoryId;

    public BestDealsDomain(){}

    public BestDealsDomain(String title, String imagePath, double price, double score, String description, int numberInCart){
        this.title = title;
        this.imagePath = imagePath;
        this. description = description;
        this.price = price;
        this.score = score;
        this.description = description;
        this.numberInCart = numberInCart;
    }

    public BestDealsDomain(ItemsModel item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.imagePath = item.getPicUrl();
        this.price = item.getPrice();
        this.score = item.getScore();
        this.categoryId = item.getCategoryId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getScore() {
        return score;
    }

    public void setStar(double score) {
        this.score = score;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void setScore(double score) {

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
