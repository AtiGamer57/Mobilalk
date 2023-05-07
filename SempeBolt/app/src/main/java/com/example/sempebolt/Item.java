package com.example.sempebolt;

public class Item {
    private String name;
    private String description;
    private String price;
    private float rating;
    private String imgRes;

    private int sold;

    //region Getter and Setters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.price = price + " Robux";
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImgRes() {
        return imgRes;
    }

    public void setImgRes(String imgRes) {
        this.imgRes = imgRes;
    }

    //endregion


    public Item(String name, String description, String price, float rating, String imgRes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.imgRes = imgRes;
    }

    public Item(String name, String description, String price, float rating, String imgRes, int sold) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.imgRes = imgRes;
        this.sold = sold;
    }

    public Item() {
    }



}
