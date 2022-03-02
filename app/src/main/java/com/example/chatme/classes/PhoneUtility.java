package com.example.chatme.classes;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.encoders.annotations.Encodable;

public class PhoneUtility {
    private String number,name,status,image;

    public PhoneUtility(String image,String number, String name,String status) {
        this.image=image;
        this.number = number;
        this.name = name;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
