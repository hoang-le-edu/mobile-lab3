package com.example.lab3;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab3.adapter.DbAdapter;
import com.example.lab3.handler.DatabaseHandler;
import com.example.lab3.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DbAdapter dbAdapter;
    private Cursor cursor;
    private List<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Part 1: Test DbAdapter with users
        dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.deleteAllUsers();

        for (int i = 0; i < 10; i++) {
            dbAdapter.createUser("Nguyễn Văn An " + i);
        }

        users = getData();
        showData();

        // Part 2: Test DatabaseHandler with contacts
        DatabaseHandler db = new DatabaseHandler(this);

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
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getId() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
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
}