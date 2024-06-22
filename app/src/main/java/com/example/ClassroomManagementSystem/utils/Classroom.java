package com.example.ClassroomManagementSystem.utils;

public class Classroom {
    private int room_id;
    private String position;
    public Classroom(int room_id, String position) {
        this.room_id = room_id;
        this.position = position;
    }
    public int getId() {
        return room_id;
    }
    public String getPosition() {
        return position;
    }
    public void setId(int room_id) {
        this.room_id = room_id;
    }
    public void setPosition(String position) {
        this.position = position;
    }
}
