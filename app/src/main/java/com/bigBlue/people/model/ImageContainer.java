package com.bigBlue.people.model;

public class ImageContainer {

    private int id, imageType;
    private String imageUri;

    public ImageContainer() {

    }

    public ImageContainer(int id, int imageType, String imageUri) {
        this.id = id;
        this.imageType = imageType;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }
}
