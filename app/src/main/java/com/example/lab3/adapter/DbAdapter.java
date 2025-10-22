package com.example.lab3.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lab3.helper.DatabaseHelper;

public class DbAdapter {
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqliteDatabase;
    private static final String DATABASE_NAME = "Database_Demo";
    private static final String DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 2;
    private final Context context;

    public DbAdapter(Context ctx) {
        this.context = ctx;
    }

    public DbAdapter open() {
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqliteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long createUser(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        return sqliteDatabase.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteUser(long rowId) {
        return sqliteDatabase.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
    }

    public boolean deleteAllUsers() {
        return sqliteDatabase.delete(DATABASE_TABLE, null, null) > 0;
    }

    public Cursor getAllUsers() {
        return sqliteDatabase.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME}, null, null, null, null, null);
    }
}

