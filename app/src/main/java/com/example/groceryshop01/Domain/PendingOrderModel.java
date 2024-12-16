package com.example.groceryshop01.Domain;

import java.io.Serializable;
import java.util.List;

public class PendingOrderModel implements Serializable {
    private String orderId;
    private String customerName;
    private String status;
    private String moneyStatus;
    private double total;
    private List<Item> itemsList;

    public static class Item implements Serializable {
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

        public int getQuantity() {
            return quantity;
        }
    }

    public PendingOrderModel() {}

    public PendingOrderModel(String orderId, String customerName, List<Item> itemsList, String status, String moneyStatus, double total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.itemsList = itemsList;
        this.status = status;
        this.moneyStatus = moneyStatus;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getStatus() {
        return status;
    }

    public String getMoneyStatus() {
        return moneyStatus;
    }

    public double getTotal() {
        return total;
    }

    public double setTotal(double total) {
        return total;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }
}
