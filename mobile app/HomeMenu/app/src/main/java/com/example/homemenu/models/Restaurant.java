package com.example.homemenu.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurant {
    private String id;
    private String name;
    private String address;
    private String imageURL;
    private ArrayList<Meniu> meniuList;


    public Restaurant() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Restaurant(String name, String address, String imageURL, ArrayList<Meniu> meniuList) {
        this.name = name;
        this.address = address;
        this.imageURL= imageURL;
        this.meniuList = meniuList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Meniu> getMeniuList() {
        return meniuList;
    }

    public void setMeniuList(ArrayList<Meniu> meniuList) {
        this.meniuList = meniuList;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
