package com.example.appptin.paciente;

import java.io.Serializable;

public class Patient implements  Serializable{

    private String token;
    private String full_name;
    private String given_name;
    private String email;
    private String phone;
    private String city;
    private String address;
    private String user_picture;

    public Patient(String token, String full_name, String given_name, String email, String phone, String city, String address, String user_picture) {
        this.token = token;
        this.full_name = full_name;
        this.given_name = given_name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.user_picture = user_picture;
    }

    public String getToken() {
        return token;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getGiven_name() {
        return given_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }
}
