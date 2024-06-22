package com.example.ClassroomManagementSystem.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import android.content.Context;
import java.sql.Connection;

public class UserDao {
    //添加数据
    public static boolean addUser(int user_id, String password, Context context) throws SQLException {
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "INSERT INTO users (user_id, password, authority) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);

            pstmt.setInt(1, user_id);
            pstmt.setString(2, password);
            pstmt.setInt(3, 0);

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
    //查询:根据id和password查询用户权限，密码错误返回-1，不存在返回-2
    public static int queryUser(int user_id, String password, Context context) throws SQLException {
        int authority = -2;
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "SELECT password, authority FROM users WHERE user_id = ?";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);
            pstmt.setInt(1, user_id);
            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String correctPassword = rs.getString("password");
                if (correctPassword.equals(password)) {
                    authority = rs.getInt("authority");
                } else {
                    authority = -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return authority;
    }
}
