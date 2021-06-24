package com.macapella.foodies;

public class ItemModel {

    String name;
    String description;
    Integer price;
    String img;
    Integer quantity;

    public ItemModel (String name, String description, Integer price, String img, Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.img = img;
        this.quantity = quantity;
    }

    public ItemModel () {

    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
