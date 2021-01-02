package com.dmt.tuan.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class DangkyThongtin extends AppCompatActivity {
    EditText et_hoten,et_password,et_repassword;
    Button btn_xacnhan_thongtin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky_thongtin);
        setTitle("Nhập thông tin");
        et_hoten = (EditText) findViewById(R.id.et_hoten);
        et_password = (EditText) findViewById(R.id.et_password);
        et_repassword = (EditText) findViewById(R.id.et_repassword);
    }
}
