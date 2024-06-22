package com.example.ClassroomManagementSystem.utils;

public class User {
    private int id;
    private String password;
    private int authority;

    public User(int id, String password, int authority) {
        this.id = id;
        this.password = password;
        this.authority = authority;
    }
    public int getUserId() {
        return id;
    }
    public void setUserId(int id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getAuthority() { return authority; }
    public void setAuthority(int authority) { this.authority = authority; }
}
