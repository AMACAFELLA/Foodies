package com.macapella.foodies;

/**
 * This class acts as the object for all Menu items pulled from the database and used through the app
 */

public class ItemModel {

    String name;
    String description;
    Integer price;
    String img;
    Integer quantity;
    String category;

    public ItemModel (String name, String description, Integer price, String img, Integer quantity, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.img = img;
        this.quantity = quantity;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
