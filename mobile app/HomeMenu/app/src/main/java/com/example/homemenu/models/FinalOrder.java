package com.example.homemenu.models;

import java.util.ArrayList;
import java.util.Date;

public class FinalOrder {
    private ArrayList<OrderItem> orderItems;
    private String addressCustomer;
    private String totalAmount;
    private Date dateOrder;

    public FinalOrder() {
    }

    public FinalOrder(ArrayList<OrderItem> orderItems, String addressCustomer, String totalAmount,Date dateOrder) {
        this.orderItems = orderItems;
        this.addressCustomer = addressCustomer;
        this.totalAmount = totalAmount;
        this.dateOrder = dateOrder;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getAddressCustomer() {
        return addressCustomer;
    }

    public void setAddressCustomer(String addressCustomer) {
        this.addressCustomer = addressCustomer;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }
}
