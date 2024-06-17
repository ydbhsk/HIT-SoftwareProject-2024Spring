package com.example.ClassroomManagementSystem.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class Reserve {
    public ArrayList<String> allReservedDate;
    public Reserve(String roomId, Context context){
        this.allReservedDate = getReserved(roomId,context);
    }

    public ArrayList<String> getReserved(String roomId,Context context){
        ReservationDao reservationDao = new ReservationDao(context);
        ArrayList<Reservation> allReservation = reservationDao.getReservationsByRoomId(roomId);
        ArrayList<String> allDate = new ArrayList<>();
        for (Reservation reservation : allReservation){
            allDate.add(reservation.getDate());
        }
        return allDate;
    }
    public static void tryReserve(String roomId, int whichClass , int whichDay, String userId, Context context){
        ReservationDao reservationDao = new ReservationDao(context);
        String date = whichDay+"-"+whichClass;
        Reservation reservation = new Reservation(date,roomId,userId,0);
        long i = -1;
        if(reservationDao.queryReservation(date,roomId) == null){
            i = reservationDao.addReservation(reservation);
        }
        String result = i == -1 ? "失败" : "成功";
        Toast.makeText(context, "预约"+ result, Toast.LENGTH_SHORT).show();
    }
    public boolean isReserved(int whichClass, int whichDay){
        String date = whichDay+"-"+whichClass;
        for (String reservedDate : this.allReservedDate){
            if (reservedDate.equals(date)){
                return true;
            }
        }
        return false;
    }
}
