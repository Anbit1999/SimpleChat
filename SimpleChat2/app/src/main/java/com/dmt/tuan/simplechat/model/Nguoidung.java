package com.dmt.tuan.simplechat.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Nguoidung {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String search;

    /**
     * for dynamodb
     */
    private String email;
    private String password;
    private String ngayTao;
    private Set<String> friends = new HashSet<String>();

    public Nguoidung(String id, String username, String imageURL,String status,String search) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public Nguoidung(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = "offline";
        this.ngayTao = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

    }

    public Nguoidung(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public Nguoidung(String email) {
        this.email = email;
    }

    public Nguoidung() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

    public void addFriend(String friendID){
        friends.add(friendID);
    }

    public void removeFriend(String friendID){
        friends.remove(friendID);
    }

    @Override
    public String toString() {
        return "Nguoidung{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", imageurl='" + imageURL + '\'' +
                '}';
    }
}
