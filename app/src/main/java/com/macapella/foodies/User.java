package com.macapella.foodies;

public class User {
    public String fullname, phone, email, account;
    public Boolean admin = false;
    public Boolean customer = true;
    public Boolean delivery = false;

    public User(){

    }

    public User(String fullname, String phone, String email, String account){
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.account = account;

    }

    public User(String toString, String toString1) {
    }

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
}
