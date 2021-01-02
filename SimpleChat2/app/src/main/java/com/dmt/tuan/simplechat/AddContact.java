package com.dmt.tuan.simplechat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {
    EditText username,phonenumber,email;
    Button btn_contact_save,btn_contact_cancel;
    Toolbar t_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        t_toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(t_toolbar);
        getSupportActionBar().setTitle("");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(AddContact.this,Trangchinh.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//            }
//        });
        setTitle("Thêm danh bạ");

        username = findViewById(R.id.username);
        phonenumber = findViewById(R.id.phonenumber);
        email = findViewById(R.id.email);

        btn_contact_save = findViewById(R.id.btn_contact_save);
        btn_contact_cancel = findViewById(R.id.btn_contact_cancel);

        btn_contact_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL,email.getText())
                        .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .putExtra(ContactsContract.Intents.Insert.PHONE, phonenumber.getText())
                        .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        .putExtra(ContactsContract.Intents.Insert.NAME, username.getText().toString());

                startActivity(intent);
                Toast.makeText(AddContact.this,"Đã lưu thông tin danh bạ thành công",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btn_contact_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddContact.this,"Không có thông tin để lưu. Đã hủy danh bạ",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}
