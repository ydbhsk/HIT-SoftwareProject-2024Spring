package com.example.mytest3.utils;

public class Classroom {
    private String id;
    private String position;
    public Classroom(String id, String position) {
        this.id = id;
        this.position = position;
    }
    public String getId() {
        return id;
    }
    public String getPosition() {
        return position;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setPosition(String position) {
        this.position = position;
    }
}
