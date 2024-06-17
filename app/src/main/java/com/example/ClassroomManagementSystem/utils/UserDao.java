package com.example.ClassroomManagementSystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
    private final SQLiteOpenHelper helper;
    public UserDao(Context context){

        helper = new SQLiteOpenHelper(context,"management_system7",null,1);
    }
    //添加数据
    public long addUser(User user){
        //获取数据库对象
        SQLiteDatabase database=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        //key是数据表的列名，value是要放进去的值
        values.put("id",user.getId());
        values.put("password",user.getPassword());
        values.put("authority",0);
        //第一个参数表明，第二个参数自动赋值为null的列名，第三个参数数据
        //返回值long,插入成功行号，插入失败-1
        long i = database.insert("users",null,values);
        //关闭数据库
        database.close();
        return i;
    }
    //查询:根据id和password查询用户权限，密码错误返回-1，不存在返回-2
    public int queryUser(String id,String password){
        SQLiteDatabase database = helper.getWritableDatabase();
        int authority = -2;
        Cursor cursor = database.query("users",null,"id=?",new String[]{id},null,null,null);
        if(cursor != null && cursor.moveToFirst()){
            int temp = cursor.getColumnIndex("password");
            if(temp != -1){
                String password1 = cursor.getString(temp);
                if(password1.equals(password)){
                    temp = cursor.getColumnIndex("authority");
                    if(temp != -1){
                        authority = cursor.getInt(temp);
                    }
                }
                else {
                    return -1;
                }
            }
            cursor.close();
        }
        return authority;
    }
    //删除
    public int deleteUser(String id){
        //获取数据库对象
        SQLiteDatabase database = helper.getWritableDatabase();
        //第一个参数表明，第二个参数为删除条件，第三个参数为第二个参数中占位符所需值组成的字符串数组
        int i = database.delete("users","id=?",new String[]{id});
        //关闭数据库
        database.close();
        return i;
    }
//    //获取用户列表
//    public ArrayList<User> queryAllUser(){
//        ArrayList<User> list = new ArrayList<User>();
//        SQLiteDatabase database = helper.getWritableDatabase();
//        Cursor cursor=database.query("users",null,null,null,null,null,null);
//        if(cursor != null&&cursor.moveToFirst()){
//            //通过游标遍历这个集合
//            do{
//                int temp = cursor.getColumnIndex("id");
//                String id = null,password = null;
//                if(temp!=-1){
//                    id = cursor.getString(temp);
//                }
//                temp = cursor.getColumnIndex("password");
//                if(temp!=-1){
//                    password = cursor.getString(temp);
//                }
//                User user=new User(id,password);
//                list.add(user);
//            }while(cursor.moveToNext());
//        }
//        return list;
//    }


}
