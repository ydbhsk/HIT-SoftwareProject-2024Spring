package com.example.ClassroomManagementSystem.utils;

/**
 * 非法输入检查
 */
public class InputCheck {
    /**
     * 检查用户名是否合法
     * @param string 用户名
     * @return 是否合法
     */
    public static boolean checkUsername(String string) {
        return string.matches("^[0-9]{6,20}$");
    }
    /**
     * 检查密码是否合法
     * @param string 密码
     * @return 是否合法
     */
    public static boolean checkPassword(String string) {
        return string.matches("^[a-zA-Z0-9]{6,20}$");
    }
    public static boolean checkRoomId(String string) {
        return string.matches("^[0-9]{1,3}$");
    }
    public static boolean checkPosition(String string) {
        return string.matches("^.{6,20}$");
    }
}
