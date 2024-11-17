package com.example.groceryshop01.Model;

import java.io.Serializable;

public interface CartItem {
    String getTitle();
    String getDescription();
    String getImagePath();  // Or picUrl depending on context
    double getPrice();
    double getScore();

    void setTitle(String title);
    void setDescription(String description);
    void setImagePath(String imagePath); // Or picUrl depending on context
    void setPrice(double price);
    void setScore(double score);

    void setCategoryId(int updatedQuantity);

    int getCategoryId();
}
