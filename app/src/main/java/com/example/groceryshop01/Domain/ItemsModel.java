package com.example.groceryshop01.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ItemsModel implements Serializable, Parcelable {

    private String name;          // Item name
    private String description;   // Item description
    private String image;         // Image URL
    private double price;         // Item price
    private int quantity;         // Quantity available
    private int categoryId;       // Category ID (if needed for filtering)
    private double score;         // Number of this item in the cart
    private String categoryKey;   // Firebase category key
    private String itemKey;       // Firebase item key

    public ItemsModel() {}

    // Constructor with parameters
    public ItemsModel(String name, String description, String image, double price, int quantity, int categoryId, double score, String categoryKey, String itemKey) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.score = score;
        this.categoryKey = categoryKey;
        this.itemKey = itemKey;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getCategoryKey() { return categoryKey; }
    public void setCategoryKey(String categoryKey) { this.categoryKey = categoryKey; }

    public String getItemKey() { return itemKey; }
    public void setItemKey(String itemKey) { this.itemKey = itemKey; }


    protected ItemsModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        image = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        categoryId = in.readInt();
        score = in.readDouble();
    }

    public static final Parcelable.Creator<ItemsModel> CREATOR = new Parcelable.Creator<ItemsModel>() {
        @Override
        public ItemsModel createFromParcel(Parcel in) {
            return new ItemsModel(in);
        }

        @Override
        public ItemsModel[] newArray(int size) {
            return new ItemsModel[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeInt(categoryId);
        dest.writeDouble(score);
    }
}
