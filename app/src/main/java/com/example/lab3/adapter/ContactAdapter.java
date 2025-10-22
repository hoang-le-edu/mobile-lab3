package com.example.lab3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lab3.R;
import com.example.lab3.model.Contact;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> contacts;

    public ContactAdapter(@NonNull Context context, @NonNull List<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        }

        Contact currentContact = contacts.get(position);

        TextView tvId = listItem.findViewById(R.id.tv_contact_id);
        TextView tvName = listItem.findViewById(R.id.tv_contact_name);
        TextView tvPhone = listItem.findViewById(R.id.tv_contact_phone);

        tvId.setText(String.valueOf(currentContact.getId()));
        tvName.setText(currentContact.getName());
        tvPhone.setText("📱 " + currentContact.getPhoneNumber());

        return listItem;
    }
}

