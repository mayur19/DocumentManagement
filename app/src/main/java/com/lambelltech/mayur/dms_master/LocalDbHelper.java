package com.lambelltech.mayur.dms_master;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by mayur on 08-01-2017.
 */

public class LocalDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SessionDB.db";
    private static final String USER_ID = "user_id";
    private static final String USER_TABLE_NAME = "userInfo";
    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_SESSION_STATUS = "session_status";
    private static int status;
    public static String username;
    public static String password;

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table userInfo(user_id INTEGER PRIMARY KEY,username TEXT,password Text,session_status INT )";
        /*
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + "("
                        + USER_ID + " INTEGER PRIMARY KEY," + USER_COLUMN_USERNAME + " TEXT,"
                        + USER_COLUMN_PASSWORD + " TEXT" + USER_COLUMN_SESSION_STATUS + "INT"+")";
                        */
        db.execSQL(query);

        String insert = "INSERT INTO userInfo (user_id, username, password, session_status) VALUES(1,'root','root',0)" ;
        db.execSQL(insert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userInfo");
        onCreate(db);
    }

    int getSessionStatus() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from userInfo",null);
        if (res.moveToFirst()) {
            status = res.getInt(3);
        }

        return status;
    }

    public String getUsername() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from userInfo", null);
        if (res.moveToFirst()) {

                username = res.getString(1);

        }
        return username;
    }

    public String getPassword(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from userInfo", null);
        if (res.moveToFirst()) {

                password = res.getString(2);
            }

        return password;
    }
    public void executeQuery(String etusername,String etpassword,int _status){
        SQLiteDatabase db = this.getReadableDatabase();
        String username,password;
        int status;
        username = etusername;
        password = etpassword;
        status = _status;
        String query = "UPDATE userInfo SET username='"+username+"',password='"+password+"',session_status="+status+";";
        db.execSQL(query);
    }
}
