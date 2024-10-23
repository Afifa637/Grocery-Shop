package com.example.groceryshop.Domain;

public class CategoryDomain {
    private String ImagePath;
    private String Name;
    private int Id;

    public CategoryDomain() {
    }

    @Override
    public String toString() {
        return Name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
