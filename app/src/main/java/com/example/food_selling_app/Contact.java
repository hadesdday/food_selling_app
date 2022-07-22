package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Contact extends AppCompatActivity {
    EditText  subjecttxt, messagetxt;
    Button btnBack, btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        subjecttxt = (EditText) findViewById(R.id.editEmailSubject);
        messagetxt = (EditText) findViewById(R.id.editMessageContact);
        btnSubmit = (Button) findViewById(R.id.btnContactSubmit);
        btnBack = (Button) findViewById(R.id.btnBackContact);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = subjecttxt.getText().toString().trim();
                String message = messagetxt.getText().toString().trim();
                String email = "ytya679@gmail.com";
                if (subject.isEmpty()) {
                    Toast.makeText(Contact.this, "Please add Subject", Toast.LENGTH_SHORT).show();
                } else if (message.isEmpty()) {
                    Toast.makeText(Contact.this, "Please add some Message", Toast.LENGTH_SHORT).show();
                } else {
                    String mail = "mailto:" + email +
                            "?&subject=" + Uri.encode(subject) +
                            "&body=" + Uri.encode(message);
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse(mail));
                    try {
                        startActivity(Intent.createChooser(intent, "Send Email.."));
                    } catch (Exception e) {
                        Toast.makeText(Contact.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}