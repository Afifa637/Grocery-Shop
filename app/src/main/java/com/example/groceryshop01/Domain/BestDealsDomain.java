package com.example.groceryshop01.Domain;

public class BestDealsDomain {
    private String title;
    private String imagePath;
    private double price;
    private double star;
    private String description;

    public BestDealsDomain(String title, String imagePath, double price, double star, String description){
        this.title = title;
        this.imagePath = imagePath;
        this. description = description;
        this.price = price;
        this.star = star;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
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
}
