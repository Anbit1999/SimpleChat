package com.dmt.tuan.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityDangKysdt_Nhapthongtin extends AppCompatActivity {
    EditText et_hoten_sdt,et_password_sdt,et_repassword_sdt;
    Button btn_dangky_sdt_xacnhan;
    ProgressBar progressBar_sdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_kysdt__nhapthongtin);

        et_hoten_sdt = (EditText) findViewById(R.id.et_hoten_sdt);
        et_hoten_sdt = (EditText) findViewById(R.id.et_hoten_sdt);
        et_hoten_sdt = (EditText) findViewById(R.id.et_hoten_sdt);

        btn_dangky_sdt_xacnhan = (Button) findViewById(R.id.btn_dangky_sdt_xacnhan);

        progressBar_sdt = (ProgressBar) findViewById(R.id.progressbar_sdt);

        FirebaseAuth auth = FirebaseAuth.getInstance();


        btn_dangky_sdt_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityDangKysdt_Nhapthongtin.this,Thongbao_Dangky_Thanhcong.class));
            }
        });
    }
}
