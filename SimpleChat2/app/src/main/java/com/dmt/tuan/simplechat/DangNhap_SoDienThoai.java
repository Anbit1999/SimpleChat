package com.dmt.tuan.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class DangNhap_SoDienThoai extends AppCompatActivity {

    EditText et_nhapsdt;
    Button btn_dangnhap_sdt_xacnhan;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap__so_dien_thoai);

        et_nhapsdt = findViewById(R.id.et_nhapsdt);
        btn_dangnhap_sdt_xacnhan = findViewById(R.id.btn_dangnhap_sdt_xacnhan);

        progressBar = findViewById(R.id.progressbar);

        btn_dangnhap_sdt_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_nhapsdt.getText().toString().trim().isEmpty()){
                    Toast.makeText(DangNhap_SoDienThoai.this,"Vui lòng nhập số điện thoại",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+84" + et_nhapsdt.getText().toString(),60, TimeUnit.SECONDS,
                        DangNhap_SoDienThoai.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                btn_dangnhap_sdt_xacnhan.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                btn_dangnhap_sdt_xacnhan.setVisibility(View.VISIBLE);
                                Toast.makeText(DangNhap_SoDienThoai.this,"Vui lòng nhập đúng số điện thoại",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String vertficationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                btn_dangnhap_sdt_xacnhan.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(DangNhap_SoDienThoai.this,ActivityDangKysdt_VerifyOTP.class);
                                intent.putExtra("phone","+84"+et_nhapsdt.getText().toString());
                                intent.putExtra("vertficationId",vertficationId);
                                startActivity(intent);
                            }
                        }
                );

            }
        });
            }
}
