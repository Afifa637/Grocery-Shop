package com.example.groceryshop01.Domain;

import java.io.Serializable;

public class BestDealsDomain implements Serializable {
    private String title;
    private String imagePath;
    private double price;
    private double score;
    private String description;
    private int numberInCart;

    public BestDealsDomain(String title, String imagePath, double price, double score, String description){
        this.title = title;
        this.imagePath = imagePath;
        this. description = description;
        this.price = price;
        this.score = score;
        this.description = description;
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

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
