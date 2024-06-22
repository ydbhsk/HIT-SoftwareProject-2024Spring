package com.example.ClassroomManagementSystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassroomDao {
    //添加数据
    public static boolean addClassroom(Classroom classroom, Context context) throws SQLException {
        //获取数据库对象
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "INSERT INTO classrooms (id, position) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);

            pstmt.setInt(1, classroom.getId());
            pstmt.setString(2, classroom.getPosition());

            int affectedRows = pstmt.executeUpdate();

            dbConn.close();
            if (affectedRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return false;
    }

    //查询:根据id查询教室位置，不存在返回null
    public static String queryClassroom(int id, Context context) throws SQLException {
        //获取数据库对象
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "SELECT position FROM classrooms WHERE id = ?";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);
            pstmt.setInt(1, id);
            java.sql.ResultSet rs = pstmt.executeQuery();
            dbConn.close();
            if (rs.next()) {
                return rs.getString("position");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return null;
    }
    //删除
    public static boolean deleteClassroom(int id, Context context) throws SQLException {
        //获取数据库对象
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "DELETE FROM classrooms WHERE id = ?";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            dbConn.close();
            if (affectedRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return false;
    }
    //获取所有教室
    public static ArrayList<Classroom> getAllClassrooms(Context context) throws SQLException {
        //获取数据库对象
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "SELECT * FROM classrooms";
        ArrayList<Classroom> classrooms = new ArrayList<>();
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);
            java.sql.ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                classrooms.add(new Classroom(rs.getInt("id"), rs.getString("position")));
            }
            dbConn.close();
            return classrooms;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return null;
    }
}
