package com.example.groceryshop01.Domain;

import java.io.Serializable;
import java.util.Map;

public class DataModel implements Serializable {
    private String category;
    private String lowDemandItems;
    private String highDemandItems;
    private String estimatedIncrease;
    private Map<String, Integer> revenue;

    public DataModel(){

    }

    public DataModel(String category, String lowDemandItems, String highDemandItems, String estimatedIncrease, Map<String, Integer> revenue) {
        this.category = category;
        this.lowDemandItems = lowDemandItems;
        this.highDemandItems = highDemandItems;
        this.estimatedIncrease = estimatedIncrease;
        this.revenue = revenue;
    }

    public String getCategory() {
        return category;
    }

    public String getLowDemandItems() {
        return lowDemandItems;
    }

    public String getHighDemandItems() {
        return highDemandItems;
    }

    public String getEstimatedIncrease() {
        return estimatedIncrease;
    }

    public Map<String, Integer> getRevenue() {
        return revenue;
    }
}
