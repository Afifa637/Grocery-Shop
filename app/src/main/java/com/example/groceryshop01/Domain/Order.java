package com.example.groceryshop01.Domain;

public class Order {
    private String customerName;
    private String moneyStatus;
    private String orderId;

    public Order() {
        // Default constructor for Firebase
    }

    public Order(String customerName,  String orderId, String moneyStatus) {
        this.customerName = customerName;
        this.moneyStatus = moneyStatus;
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMoneyStatus() {
        return moneyStatus;
    }

    public void setMoneyStatus(String moneyStatus) {
        this.moneyStatus = moneyStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
