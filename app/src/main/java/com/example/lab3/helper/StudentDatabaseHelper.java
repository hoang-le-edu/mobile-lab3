package com.example.lab3.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lab3.model.Student;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "qlsv.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "sinhvien";
    private static final String COLUMN_MSSV = "MSSV";
    private static final String COLUMN_HOTEN = "Hoten";
    private static final String COLUMN_LOP = "Lop";

    private Context context;
    private String databasePath;
    private SQLiteDatabase database;

    public StudentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
    }
    
    /**
     * Open database - Call this to keep connection open for Database Inspector
     */
    public void openDatabase() {
        if (database == null || !database.isOpen()) {
            database = this.getWritableDatabase();
            Log.d("StudentDB", "Database opened");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng sinhvien nếu chưa có
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_MSSV + " TEXT PRIMARY KEY, "
                + COLUMN_HOTEN + " TEXT, "
                + COLUMN_LOP + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Copy database từ assets vào internal storage
     */
    public void copyDatabaseFromAssets() {
        try {
            File dbFile = context.getDatabasePath(DATABASE_NAME);
            
            // Nếu database đã tồn tại, không copy nữa
            if (dbFile.exists()) {
                Log.d("Database", "Database already exists, skipping copy");
                return;
            }

            // Tạo thư mục nếu chưa có
            dbFile.getParentFile().mkdirs();

            // Copy database từ assets
            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            OutputStream myOutput = new FileOutputStream(dbFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();

            Log.d("Database", "Database copied successfully from assets");
        } catch (Exception e) {
            Log.e("Database", "Error copying database from assets: " + e.getMessage());
            e.printStackTrace();
            // Nếu không có file trong assets, tạo database mới
            onCreate(getWritableDatabase());
        }
    }

    // CRUD Operations

    public long insertStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MSSV, student.getMssv());
        values.put(COLUMN_HOTEN, student.getHoten());
        values.put(COLUMN_LOP, student.getLop());
        long result = db.insert(TABLE_NAME, null, values);
        Log.d("StudentDB", "Insert result: " + result + " for MSSV: " + student.getMssv());
        return result;
    }

    public int deleteStudent(String mssv) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_MSSV + "=?", new String[]{mssv});
        Log.d("StudentDB", "Delete result: " + result + " rows for MSSV: " + mssv);
        return result;
    }

    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOTEN, student.getHoten());
        values.put(COLUMN_LOP, student.getLop());
        int result = db.update(TABLE_NAME, values, COLUMN_MSSV + "=?", new String[]{student.getMssv()});
        Log.d("StudentDB", "Update result: " + result + " rows for MSSV: " + student.getMssv());
        return result;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_MSSV);

        Log.d("StudentDB", "getAllStudents - Cursor count: " + cursor.getCount());
        
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setMssv(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MSSV)));
                student.setHoten(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOTEN)));
                student.setLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOP)));
                students.add(student);
                Log.d("StudentDB", "Loaded: " + student.getMssv() + " - " + student.getHoten());
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("StudentDB", "Total students loaded: " + students.size());
        return students;
    }

    public Student getStudentByMssv(String mssv) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_MSSV + "=?", 
                new String[]{mssv}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Student student = new Student();
            student.setMssv(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MSSV)));
            student.setHoten(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOTEN)));
            student.setLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOP)));
            cursor.close();
            return student;
        }
        return null;
    }

    // Debug method
    public void debugDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        
        // Check if table exists
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", 
                new String[]{TABLE_NAME});
        if (cursor.moveToFirst()) {
            Log.d("StudentDB", "Table '" + TABLE_NAME + "' exists");
        } else {
            Log.e("StudentDB", "Table '" + TABLE_NAME + "' does NOT exist!");
        }
        cursor.close();
        
        // Get table info
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);
            Log.d("StudentDB", "Table structure:");
            while (cursor.moveToNext()) {
                String colName = cursor.getString(1);
                String colType = cursor.getString(2);
                Log.d("StudentDB", "  Column: " + colName + " (" + colType + ")");
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("StudentDB", "Error getting table info: " + e.getMessage());
        }
        
        // Count rows
        cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            Log.d("StudentDB", "Total rows in table: " + count);
        }
        cursor.close();
    }
}

