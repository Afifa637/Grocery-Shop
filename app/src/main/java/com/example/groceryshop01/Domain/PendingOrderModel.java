package com.example.groceryshop01.Domain;

import java.io.Serializable;
import java.util.List;

public class PendingOrderModel implements Serializable {
    private String customerName;
    private String status;
    private List<Item> itemsList;

    // Default constructor required for Firestore
    public PendingOrderModel() {}

    public PendingOrderModel(String customerName, List<Item> itemsList) {
        this.customerName = customerName;
        this.status = "pending"; // Default status
        this.itemsList = itemsList;
    }

    // Getters and setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    // Nested class for items
    public static class Item {
        private String name;
        private int quantity;

        public Item() {}

        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
