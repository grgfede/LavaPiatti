package com.frarisstudio.lavapiatti.system;

import java.io.Serializable;

public class User implements Serializable {

    private String uid, name, surname, email;

    public User(String uid, String n, String s, String e){
        this.uid = uid;
        name = n;
        surname = s;
        email = s;
    }

    public String getUid (){return uid;}

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
