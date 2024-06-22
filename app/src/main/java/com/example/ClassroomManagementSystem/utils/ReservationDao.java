package com.example.ClassroomManagementSystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReservationDao{
    //添加数据
    public static boolean addReservation(Reservation reservation, Context context) throws SQLException {
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "INSERT INTO reservations (date, room_id, user_id, result) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);

            pstmt.setString(1, reservation.getDate());
            pstmt.setInt(2, reservation.getRoomId());
            pstmt.setInt(3, reservation.getUserId());
            pstmt.setInt(4, reservation.getResult());

            int affectedRows = pstmt.executeUpdate();

            dbConn.close();
            if (affectedRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return false;
    }
    //删除
    public static boolean deleteReservation(String date, int roomId, Context context) throws SQLException {
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "DELETE FROM reservations WHERE date = ? AND room_id = ?";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setInt(2, roomId);
            int affectedRows = pstmt.executeUpdate();
            dbConn.close();
            if (affectedRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return false;
    }
    //更新:根据room_id和date,以result更新预约信息，返回更新行数
    public static boolean updateReservation(String date, int roomId, int result, Context context) throws SQLException {
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "UPDATE reservations SET result = ? WHERE date = ? AND room_id = ?";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);
            pstmt.setInt(1, result);
            pstmt.setString(2, date);
            pstmt.setInt(3, roomId);
            int affectedRows = pstmt.executeUpdate();
            dbConn.close();
            if (affectedRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return false;
    }
    //查询:根据room_id和date查询预约信息，不存在返回null
    public static Reservation queryReservation(String date, int roomId, Context context) throws SQLException {
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "SELECT * FROM reservations WHERE date = ? AND room_id = ?";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setInt(2, roomId);
            ResultSet rs = pstmt.executeQuery();
            dbConn.close();
            if (rs.next()) {
                return new Reservation(rs.getString("date"), rs.getInt("room_id"),
                        rs.getInt("user_id"), rs.getInt("result"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }dbConn.close();
        return null;
    }
    //获取某room所有预约信息
    public static ArrayList<Reservation> getReservationsByRoomId(int roomId, Context context) throws SQLException {
        ArrayList<Reservation> allReservation = getAllReservations(context);
        ArrayList<Reservation> allReservationOfRoom = new ArrayList<>();
        assert allReservation != null;
        for(Reservation reservation:allReservation){
            int temp = reservation.getRoomId();
            if(temp == roomId){
                allReservationOfRoom.add(reservation);
            }
        }
        return allReservationOfRoom;
    }
    //获取某用户所有预约信息
    public static ArrayList<Reservation> getReservationsByUserId(int userId, Context context) throws SQLException {
        ArrayList<Reservation> allReservation = getAllReservations(context);
        ArrayList<Reservation> userReservation = new ArrayList<>();
        assert allReservation != null;
        for(Reservation reservation:allReservation){
            if(reservation.getUserId() == userId){
                userReservation.add(reservation);
            }
        }
        return userReservation;
    }
    //获取所有未处理预约信息
    public static ArrayList<Reservation> getUnprocessedReservations(Context context) throws SQLException {
        ArrayList<Reservation> allReservation = getAllReservations(context);
        ArrayList<Reservation> UnproReservation = new ArrayList<>();
        assert allReservation != null;
        for(Reservation reservation:allReservation){
            if(reservation.getResult() == 0){
                UnproReservation.add(reservation);
            }
        }
        return UnproReservation;
    }
    //获取所有预约信息
    public static ArrayList<Reservation> getAllReservations(Context context) throws SQLException {
        ArrayList<Reservation> allReservation = new ArrayList<>();
        Connection dbConn = WebConnect.getConnection(context);
        String sql = "SELECT * FROM reservations";
        try {
            PreparedStatement pstmt = dbConn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                allReservation.add(new Reservation(rs.getString("date"), rs.getInt("room_id"), rs.getInt("user_id"), rs.getInt("result")));
            }
            dbConn.close();
            return allReservation;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConn.close();
        return null;
    }
}
