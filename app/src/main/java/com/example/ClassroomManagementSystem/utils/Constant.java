package com.example.ClassroomManagementSystem.utils;

import java.util.HashMap;

public class Constant {
    public static HashMap<Integer,String> weekDays = new HashMap<Integer,String>(){{
        put(1,"星期一");
        put(2,"星期二");
        put(3,"星期三");
        put(4,"星期四");
        put(5,"星期五");
        put(6,"星期六");
        put(7,"星期日");
    }};

    public static int bottomNavigationHeight = 100;
    public static int TopNavigationHeight = 50;
    public static int GridHeight = 50;
}
