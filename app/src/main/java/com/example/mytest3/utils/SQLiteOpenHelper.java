package com.example.mytest3.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper{
    private Context context;
    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLES="create table users ("+
                "id text PRIMARY KEY,"+
                "password TEXT,"+
                "authority INTEGER)";
        final String CREATE_CLASSROOMS="create table classrooms ("+
                "id text PRIMARY KEY,"+
                "position TEXT)";

        final String CREATE_RESERVATIONS="create table reservations ("+
                "date TEXT,"+ // 使用 text 类型存储日期
                "room_id TEXT,"+
                "user_id TEXT,"+
                "result INTEGER,"+
                "PRIMARY KEY (date, room_id))";
        db.execSQL(CREATE_TABLES);
        db.execSQL(CREATE_CLASSROOMS);
        db.execSQL(CREATE_RESERVATIONS);
        Toast.makeText(context, "success databases", Toast.LENGTH_SHORT).show();
        final String INSERT_ADMIN="insert into users values ('admin','123',1)";
        db.execSQL(INSERT_ADMIN);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

//    public void dropTable(String tableName){
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("drop table "+tableName);
//        db.close();
//    }
}
