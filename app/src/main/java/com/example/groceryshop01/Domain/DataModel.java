package com.example.groceryshop01.Domain;

import java.io.Serializable;

public class DataModel implements Serializable {
    private String category;
    private String lowDemandItems;
    private String highDemandItems;
    private String estimatedIncrease; // Changed 'revenue' to estimated growth percentage.

    public DataModel(String category, String lowDemandItems, String highDemandItems, String estimatedIncrease) {
        this.category = category;
        this.lowDemandItems = lowDemandItems;
        this.highDemandItems = highDemandItems;
        this.estimatedIncrease = estimatedIncrease;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLowDemandItems() {
        return lowDemandItems;
    }

    public void setLowDemandItems(String lowDemandItems) {
        this.lowDemandItems = lowDemandItems;
    }

    public String getHighDemandItems() {
        return highDemandItems;
    }

    public void setHighDemandItems(String highDemandItems) {
        this.highDemandItems = highDemandItems;
    }

    public String getEstimatedIncrease() {
        return estimatedIncrease;
    }

    public void setEstimatedIncrease(String estimatedIncrease) {
        this.estimatedIncrease = estimatedIncrease;
    }
}
