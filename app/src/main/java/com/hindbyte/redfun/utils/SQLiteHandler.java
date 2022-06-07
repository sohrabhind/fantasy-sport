package com.hindbyte.redfun.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "REDFUN_DATABASE";
    private static final String TABLE_USER = "user";

    private static final String KEY_ID = "id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASS = "pass";
    private static final String KEY_NAME = "name";
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_STATUS = "status";
   
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PHONE + " TEXT UNIQUE,"
                + KEY_PASS + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_BALANCE + " TEXT,"
                + KEY_STATUS + " TEXT"+ ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUser(String id, String phone, String pass, String name, String balance, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_PHONE, phone);
        values.put(KEY_PASS, pass);
        values.put(KEY_NAME, name);
        values.put(KEY_BALANCE, balance);
        values.put(KEY_STATUS, status);
        db.insert(TABLE_USER, null, values);
        db.close();
        Log.d(TAG, "New user inserted into sqLite: " + id);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(0));
            user.put("phone", cursor.getString(1));
            user.put("pass", cursor.getString(2));
            user.put("name", cursor.getString(3));
            user.put("balance", cursor.getString(4));
            user.put("status", cursor.getString(5));
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
        return user;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
        Log.d(TAG, "Deleted all user info from sqlite");
    }
}
