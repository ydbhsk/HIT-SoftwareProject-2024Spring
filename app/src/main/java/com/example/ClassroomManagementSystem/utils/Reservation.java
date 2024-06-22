package com.example.ClassroomManagementSystem.utils;

public class Reservation {
    private int userId;
    private String date;
    private int roomId;
    private int result;
    public Reservation(String date, int roomId, int userId, int result) {
        this.date = date;
        this.roomId = roomId;
        this.userId = userId;
        this.result = result;
    }
    public int getUserId() {
        return userId;
    }
    public String getDate() {
        return date;
    }
    public int getRoomId() {
        return roomId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public int getResult() {
        return result;
    }
    public void setResult(int result) {
        this.result = result;
    }
}
