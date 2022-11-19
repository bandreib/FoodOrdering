package com.example.homemenu.models;

public class OrderItem {
    private String Food;
    private String Quantity;
    private String Price;


    public OrderItem() {
    }

    public OrderItem(String food, String quantity,String price) {
        Food = food;
        Quantity = quantity;
        Price = price;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String food) {
        Food = food;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
