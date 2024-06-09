package com.example.mytest3.utils;

import android.content.Context;

import java.util.ArrayList;

public class RoomList {
    public static ArrayList<Classroom> getRoomList(Context context){
        ClassroomDao classroomDao = new ClassroomDao(context);
        return classroomDao.getAllClassrooms();
    }
}
