package com.dmt.tuan.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityDangky extends AppCompatActivity {
    Button btn_dangky_sdt,btn_dangky_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        setTitle("Đăng ký tài khoản");
        btn_dangky_email = (Button) findViewById(R.id.btn_dangky_email);
        btn_dangky_sdt = (Button) findViewById(R.id.btn_dangky_sdt);

        btn_dangky_sdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDangky.this,ActivityDangKysdt_SendOTP.class);
                startActivity(intent);
            }
        });

        btn_dangky_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDangky.this, ActivityDangky_Email.class);
                startActivity(intent);
            }
        });
    }
}
