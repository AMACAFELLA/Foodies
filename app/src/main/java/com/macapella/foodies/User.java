package com.macapella.foodies;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class User implements Serializable {

    @Exclude
    // Declaring the user data
    private String key;
    public String fullname;
    public String phone;
    public String email;
    public String account;
    public Boolean admin = false;
    public Boolean customer = true;
    public Boolean delivery = false;

    public User(){

    }
    //Assigning the data
    public User(String fullname, String phone, String email, String account){
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.account = account;

    }

    public User(String toString, String toString1) {
    }
    //Getting the data
    public String getFullname()
    {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.phone = account;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getCustomer() {
        return customer;
    }

    public void setCustomer(Boolean customer) {
        this.customer = customer;
    }

    public Boolean getDelivery() {
        return delivery;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }
}