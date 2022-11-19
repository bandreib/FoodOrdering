package com.example.homemenu.models;

import java.util.Date;

public class UserOrder {
    String restaurantName;
    String statusOrder;
    String orderAddress;
    String orderAmount;
    Date orderDate;

    public UserOrder() {
    }

    public UserOrder(String restaurantName, String statusOrder, String orderAddress, String orderAmount, Date orderDate) {
        this.restaurantName = restaurantName;
        this.statusOrder = statusOrder;
        this.orderAddress = orderAddress;
        this.orderAmount = orderAmount;
        this.orderDate = orderDate;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
