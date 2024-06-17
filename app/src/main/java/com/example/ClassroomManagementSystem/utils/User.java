package com.example.ClassroomManagementSystem.utils;

public class User {
    private String id;
    private String password;
    private int authority;

    public User(String id, String password, int authority) {
        this.id = id;
        this.password = password;
        this.authority = authority;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
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
