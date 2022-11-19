package com.example.homemenu.models;

public class Meniu {
    private String food;
    private String description;
    private String price;


    public Meniu() {
    }

    public Meniu(String food, String description, String price) {
        this.food = food;
        this.description = description;
        this.price = price;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
