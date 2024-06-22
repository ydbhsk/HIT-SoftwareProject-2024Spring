package com.example.ClassroomManagementSystem.utils;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;

public class RoomList {
    public static ArrayList<Classroom> getRoomList(Context context) throws SQLException {
        return ClassroomDao.getAllClassrooms(context);
    }
}
