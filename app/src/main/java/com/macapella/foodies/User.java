package com.macapella.foodies;

public class User {
    public String fullname, phone, email;
    public Boolean admin = false;
    public Boolean customer = true;
    public Boolean delivery = false;

    public User(){

    }

    public User(String fullname, String phone, String email){
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
    }

}
