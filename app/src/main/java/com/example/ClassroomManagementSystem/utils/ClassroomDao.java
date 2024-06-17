package com.example.ClassroomManagementSystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ClassroomDao {
    private final SQLiteOpenHelper helper;
    public ClassroomDao(Context context){
        helper = new SQLiteOpenHelper(context,"management_system7",null,1);
    }
    //添加数据
    public long addClassroom(Classroom classroom){
        //获取数据库对象
        SQLiteDatabase database=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        //key是数据表的列名，value是要放进去的值
        values.put("id",classroom.getId());
        values.put("position",classroom.getPosition());
        //第一个参数表明，第二个参数自动赋值为null的列名，第三个参数数据
        //返回值long,插入成功行号，插入失败-1
        long i = database.insert("classrooms",null,values);
        //关闭数据库
        database.close();
        return i;
    }
    //查询:根据id查询教室位置，不存在返回null
    public String queryClassroom(String id){
        SQLiteDatabase database = helper.getWritableDatabase();
        String position = null;
        Cursor cursor = database.query("classrooms",null,"id=?",new String[]{id},null,null,null);
        if(cursor != null && cursor.moveToFirst()){
            int temp = cursor.getColumnIndex("position");
            if(temp != -1){
                position = cursor.getString(temp);
            }
            cursor.close();
        }
        return position;
    }
    //删除
    public long deleteClassroom(String id){
        //获取数据库对象
        SQLiteDatabase database = helper.getWritableDatabase();
        //第一个参数表明，第二个参数为删除条件，第三个参数为第二个参数中占位符所需值组成的字符串数组
        long i = database.delete("classrooms","id=?",new String[]{id});
        //关闭数据库
        database.close();
        return i;
    }
    //获取所有教室
    public ArrayList<Classroom> getAllClassrooms(){
        ArrayList<Classroom> allClassroom = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.query("classrooms",null,null,null,null,null,null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                int temp = cursor.getColumnIndex("id");
                String id = null,position = null;
                if(temp != -1){
                    id = cursor.getString(temp);
                }
                temp = cursor.getColumnIndex("position");
                if(temp != -1){
                    position = cursor.getString(temp);
                }
                allClassroom.add(new Classroom(id,position));
            }while(cursor.moveToNext());
            cursor.close();
        }
        return allClassroom;
    }
}
