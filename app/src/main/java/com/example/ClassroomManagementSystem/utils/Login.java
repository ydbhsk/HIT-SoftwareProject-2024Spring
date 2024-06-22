package com.example.ClassroomManagementSystem.utils;

import android.content.Context;
import android.widget.Toast;

public class Login {
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

//try{
//        UserDao.addUser(User(id, password, 0), context)
//        Toast.makeText(context, "已自动注册", Toast.LENGTH_SHORT).show()
//                                }catch (e: Exception){
//        Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show()
//                                }