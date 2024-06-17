package com.example.ClassroomManagementSystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ReservationDao{
    private final SQLiteOpenHelper helper;
    public ReservationDao(Context context){
        helper = new SQLiteOpenHelper(context,"management_system7",null,1);
    }
    //添加数据
    public long addReservation(Reservation reservation){
        //获取数据库对象
        SQLiteDatabase database=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        //key是数据表的列名，value是要放进去的值
        values.put("date",reservation.getDate());
        values.put("room_id",reservation.getRoomId());
        values.put("user_id",reservation.getUserId());
        values.put("result",reservation.getResult());
        //第一个参数表明，第二个参数自动赋值为null的列名，第三个参数数据
        //返回值long,插入成功行号，插入失败-1
        long i = -1;
        i = database.insert("reservations",null,values);
        //弹出提示信息
        //关闭数据库
        database.close();
        return i;
    }
    //删除
    public long deleteReservation(String date, String roomId){
        //获取数据库对象
        SQLiteDatabase database = helper.getWritableDatabase();
        //第一个参数表明，第二个参数为删除条件，第三个参数为第二个参数中占位符所需值组成的字符串数组
        long i = database.delete("reservations","date=? and room_id=?",new String[]{date,roomId});
        //关闭数据库
        database.close();
        return i;
    }
    //更新:根据room_id和date,以result更新预约信息，返回更新行数
    public long updateReservation(String date, String roomId, int result){
        //获取数据库对象
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("result",result);
        //第一个参数表明，第二个参数为更新数据，第三个参数为更新条件，第四个参数为第三个参数中占位符所需值组成的字符串数组
        long i = database.update("reservations",values,"date=? and room_id=?",new String[]{date,roomId});
//        deleteReservation(date,roomId);
//        Reservation reservation = new Reservation(date,roomId,userId,result);
//        long i = addReservation(reservation);
        //关闭数据库
        database.close();
        return i;
    }
    //查询:根据room_id和date查询预约信息，不存在返回null
    public Reservation queryReservation(String date, String roomId){
        SQLiteDatabase database = helper.getWritableDatabase();
        Reservation reservation = null;
        Cursor cursor = database.query("reservations",null,"date=? and room_id=?",new String[]{date,roomId},null,null,null);
        if(cursor != null && cursor.moveToFirst()){
            int temp1 = cursor.getColumnIndex("date");
            int temp2 = cursor.getColumnIndex("room_id");
            int temp3 = cursor.getColumnIndex("user_id");
            int temp4 = cursor.getColumnIndex("result");
            if(temp1 != -1 && temp2 != -1 && temp3 != -1 && temp4 != -1){
                reservation = new Reservation(cursor.getString(temp1),cursor.getString(temp2),
                        cursor.getString(temp3),cursor.getInt(temp4));
            }
            cursor.close();
        }
        return reservation;
    }
    //获取某room所有预约信息
    public ArrayList<Reservation> getReservationsByRoomId(String roomId){
        ArrayList<Reservation> allReservation = getAllReservations();
        ArrayList<Reservation> allReservationOfRoom = new ArrayList<>();
        for(Reservation reservation:allReservation){
            String temp = reservation.getRoomId();
            if(temp.equals(roomId)){
                allReservationOfRoom.add(reservation);
            }
        }
        return allReservationOfRoom;
    }
    //获取某用户所有预约信息
    public ArrayList<Reservation> getReservationsByUserId(String userId){
        ArrayList<Reservation> allReservation = getAllReservations();
        ArrayList<Reservation> userReservation = new ArrayList<>();
        for(Reservation reservation:allReservation){
            if(reservation.getUserId().equals(userId)){
                userReservation.add(reservation);
            }
        }
        return userReservation;
    }
    //获取所有未处理预约信息
    public ArrayList<Reservation> getUnprocessedReservations(){
        ArrayList<Reservation> allReservation = getAllReservations();
        ArrayList<Reservation> UnproReservation = new ArrayList<>();
        for(Reservation reservation:allReservation){
            if(reservation.getResult() == 0){
                UnproReservation.add(reservation);
            }
        }
        return UnproReservation;
    }
    //获取所有预约信息
    public ArrayList<Reservation> getAllReservations(){
        ArrayList<Reservation> allReservation = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.query("reservations",null,null,null,null,null,null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                int temp1 = cursor.getColumnIndex("date");
                int temp2 = cursor.getColumnIndex("room_id");
                int temp3 = cursor.getColumnIndex("user_id");
                int temp4 = cursor.getColumnIndex("result");
                if(temp1 != -1 && temp2 != -1 && temp3 != -1 && temp4 != -1){
                    allReservation.add(new Reservation(cursor.getString(temp1),cursor.getString(temp2),
                            cursor.getString(temp3),cursor.getInt(temp4)));
                }
            }while(cursor.moveToNext());
            cursor.close();
        }
        return allReservation;
    }
}
