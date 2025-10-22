package com.example.lab3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab3.adapter.ContactAdapter;
import com.example.lab3.adapter.DbAdapter;
import com.example.lab3.handler.DatabaseHandler;
import com.example.lab3.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DbAdapter dbAdapter;
    private Cursor cursor;
    private List<String> users;
    
    private DatabaseHandler db;
    private List<Contact> contacts;
    private ContactAdapter contactAdapter;
    private Button btnGotoStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to navigate to Student Management
        btnGotoStudent = findViewById(R.id.btn_goto_student);
        btnGotoStudent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentActivity.class);
            startActivity(intent);
        });

        // Part 1: Test DbAdapter with users (chạy ngầm để test)
        dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.deleteAllUsers();

        for (int i = 0; i < 10; i++) {
            dbAdapter.createUser("Nguyễn Văn An " + i);
        }

        users = getData();
        // showData(); // Commented out vì giờ hiển thị Contact

        // Part 2: Test DatabaseHandler with contacts
        db = new DatabaseHandler(this);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new Contact("Ravi", "9100000000"));
        db.addContact(new Contact("Srinivas", "9199999999"));
        db.addContact(new Contact("Tommy", "9522222222"));
        db.addContact(new Contact("Karthik", "9533333333"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getId() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        // Hiển thị Contact list lên ListView
        showContactData();
        
        // Xử lý long click để xóa contact
        setupLongClickListener();
    }

    private List<String> getData() {
        List<String> users = new ArrayList<>();

        cursor = dbAdapter.getAllUsers();
        while (cursor.moveToNext()) {
            users.add(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.KEY_NAME)));
        }

        return users;
    }

    private void showData() {
        ListView lvUser = findViewById(R.id.lv_user);
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.item_user, users);
        lvUser.setAdapter(userAdapter);
    }

    private void showContactData() {
        ListView lvUser = findViewById(R.id.lv_user);
        contactAdapter = new ContactAdapter(this, contacts);
        lvUser.setAdapter(contactAdapter);
    }

    private void setupLongClickListener() {
        ListView lvUser = findViewById(R.id.lv_user);
        
        lvUser.setOnItemLongClickListener((parent, view, position, id) -> {
            // Lấy contact tại vị trí được long click
            Contact contactToDelete = contacts.get(position);
            
            // Xóa contact khỏi database
            db.deleteContact(contactToDelete);
            
            // Xóa contact khỏi list
            contacts.remove(position);
            
            // Cập nhật lại ListView
            refreshContactList();
            
            // Hiển thị thông báo
            Toast.makeText(MainActivity.this, 
                    "Đã xóa: " + contactToDelete.getName(), 
                    Toast.LENGTH_SHORT).show();
            
            // Log
            Log.d("Delete: ", "Deleted contact: " + contactToDelete.getName());
            
            return true; // Consume the long click event
        });
    }

    private void refreshContactList() {
        contactAdapter.notifyDataSetChanged();
    }
}