package com.example.ClassroomManagementSystem.utils;

import android.content.Context;
import android.widget.Toast;

//import com.example.ClassroomManagementSystem.AdminActivity;


public class Login {
    public static int tryLog(String id, String password, Context context){
        UserDao userDao = new UserDao(context);
        int authority = userDao.queryUser(id,password);
        switch (authority){
            case -1: {
                Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                return -1;
            }
            case -2:
                Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
                userDao.addUser(new User(id,password,0));
                Toast.makeText(context, "已自动注册", Toast.LENGTH_SHORT).show();
                return -2;
            case 0:
                Toast.makeText(context, "成功登入", Toast.LENGTH_SHORT).show();
                return 0;
            case 1:
                Toast.makeText(context, "管理员模式", Toast.LENGTH_SHORT).show();
                return 1;
        }
        Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
        return -3;
    }
    public static String chooseActivity(String id, int authority, Context context){
        String nav;
        switch (authority){
            case 0:
                nav = "userScreen/"+id;
                break;
            case 1:
                nav = "adminScreen";
                break;
            default:
                nav = "homeScreen";
        }
        return nav;
    }
}
