package com.example.mytest3.utils;

public class Reservation {
    private String userId;
    private String date;
    private String roomId;
    private int result;
    public Reservation(String date, String roomId, String userId, int result) {
        this.date = date;
        this.roomId = roomId;
        this.userId = userId;
        this.result = result;
    }
    public String getUserId() {
        return userId;
    }
    public String getDate() {
        return date;
    }
    public String getRoomId() {
        return roomId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public int getResult() {
        return result;
    }
    public void setResult(int result) {
        this.result = result;
    }
}
