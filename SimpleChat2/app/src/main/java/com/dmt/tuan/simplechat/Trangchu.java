package com.dmt.tuan.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import com.dmt.tuan.simplechat.khoa.client.ServerInfo;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.URISyntaxException;

public class Trangchu extends AppCompatActivity {
    Button btn_dangnhap,btn_dangky;
    FirebaseUser user;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.89.2:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onStart() {
        super.onStart();
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        String email =  user.getEmail();
//        ServerInfo.nguoidung.setEmail(email);
        if (user != null){
            Intent intent = new Intent(Trangchu.this,Trangchinh.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_dangky = (Button) findViewById(R.id.btn_dangky);
        btn_dangnhap = (Button) findViewById(R.id.btn_dangnhap);

        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Trangchu.this,Dangnhap_TaiKhoan.class));
            }
        });

        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trangchu.this,ActivityDangky.class);
                startActivityForResult(intent,9999);
            }
        });
        mSocket.connect();
    }
}
