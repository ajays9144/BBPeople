package com.bigBlue.people.model;

import java.util.List;

public class UserModel {

    private String userName, phoneNumber;
    private String about;

    private List<ImageContainer> imageContainers;

    public UserModel() {

    }

    public UserModel(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<ImageContainer> getImageContainers() {
        return imageContainers;
    }

    public void setImageContainers(List<ImageContainer> imageContainers) {
        this.imageContainers = imageContainers;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
