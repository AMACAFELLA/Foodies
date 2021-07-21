package com.macapella.foodies;

import java.util.List;

/**
 * Is the class for the Order object that is used when retrieving order information from Firebase and throughout the app
 */

public class OrderModel {

    String name;
    String orderNumber;
    String status;
    String phone;
    String latitude;
    String longitude;
    String price;
    String totalToPay;
    List<ItemModel> itemModelList;

    public OrderModel (String name, String orderNumber, String status, String phone, String latitude, String longitude, String price, String totalToPay, List<ItemModel> itemModelList) {
        this.name = name;
        this.orderNumber = orderNumber;
        this.status = status;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.totalToPay = totalToPay;
        this.itemModelList = itemModelList;
    }

    public OrderModel () {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(String totalToPay) {
        this.totalToPay = totalToPay;
    }

    public List<ItemModel> getItemModelList() {
        return itemModelList;
    }

    public void setItemModelList(List<ItemModel> itemModelList) {
        this.itemModelList = itemModelList;
    }
}
