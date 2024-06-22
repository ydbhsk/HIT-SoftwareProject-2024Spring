package com.example.ClassroomManagementSystem.utils;
import android.content.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WebConnect {
    public static Connection getConnection(Context context) {
        String userName = "myroot";
        String userPwd = "Zsb0203rds2330";
        Connection dbConn = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //加上 useunicode=true;characterEncoding=UTF-8 防止中文乱码
            dbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" +
                    "rm-2ze5s63zbrug0k2n9go.sqlserver.rds.aliyuncs.com:3433;DatabaseName=management_system"
                    , userName, userPwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConn;
    }
    public static boolean testConnect(Context context) throws SQLException {
        Connection dbConn = getConnection(context);
        if(dbConn == null){
            dbConn.close();
            return false;
        }else {
            return true;
        }
    }
}
