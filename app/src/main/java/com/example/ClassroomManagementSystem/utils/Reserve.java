package com.example.ClassroomManagementSystem.utils;

import android.content.Context;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class Reserve {
    public ArrayList<String> allReservedDate;
    public Reserve(int roomId, Context context) throws SQLException {
        this.allReservedDate = getReserved(roomId,context);
    }

    public ArrayList<String> getReserved(int roomId,Context context) throws SQLException {
        ArrayList<Reservation> allReservation = ReservationDao.getReservationsByRoomId(roomId,context);
        ArrayList<String> allDate = new ArrayList<>();
        for (Reservation reservation : allReservation){
            if(reservation.getResult() == 0 || reservation.getResult() == 1){
                allDate.add(reservation.getDate());
            }
        }
        return allDate;
    }
    public static void tryReserve(int roomId, int whichClass , int whichDay, int userId, Context context) throws SQLException {
        String date = whichDay+"-"+whichClass;
        Reservation reservation = new Reservation(date,roomId,userId,0);
        Reservation reservationInDB = ReservationDao.queryReservation(date,roomId,context);
        if(reservationInDB == null || reservationInDB.getResult() == 0 || reservationInDB.getResult() == 1){
            ReservationDao.addReservation(reservation,context);
        }
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
