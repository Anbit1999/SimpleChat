package com.dmt.tuan.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Thongbao_Dangky_Thanhcong extends AppCompatActivity {
    Button btn_quayve_trangchu;
    ProgressBar progressBar_quayve_trangchu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongbao__dangky__thanhcong);
        setTitle("Thông báo");
        btn_quayve_trangchu = (Button) findViewById(R.id.btn_dangky_quayve_trangchu);
        progressBar_quayve_trangchu = (ProgressBar) findViewById(R.id.progressbar_thongbao_thanhcong);

        btn_quayve_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_quayve_trangchu.setVisibility(View.VISIBLE);
                startActivity(new Intent(Thongbao_Dangky_Thanhcong.this, Trangchu.class));
            }
        });
    }
}
