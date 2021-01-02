package com.dmt.tuan.simplechat.model;

public class Contact {
    private String username;
    private String phone;
    private String image_user;

    public Contact(String username, String phone, String image_user) {
        this.username = username;
        this.phone = phone;
        this.image_user = image_user;
    }

    public Contact() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }


}
