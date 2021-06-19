package com.macapella.foodies;

public class User {
    public String fullname, age, email;
    public Boolean admin = false;
    public Boolean customer = true;
    public Boolean delivery = false;

    public User(){

    }

    public User(String fullname, String age, String email){
        this.fullname = fullname;
        this.age = age;
        this.email = email;
    }

}
