package com.example.ClassroomManagementSystem.utils;

public class RuntimeInfo {
    public User loggedUser;
    public Classroom selectRoom;
    public void setLoggedUser(User loggedUser) {this.loggedUser = loggedUser;}
    public User getLoggedUser() {
        return loggedUser;
    }
    public void setSelectRoom(Classroom selectRoom) {this.selectRoom = selectRoom;}
    public Classroom getSelectRoom() {return selectRoom;}

}
