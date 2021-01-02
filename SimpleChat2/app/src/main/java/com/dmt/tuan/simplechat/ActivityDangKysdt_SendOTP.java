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

public class ActivityDangKysdt_SendOTP extends AppCompatActivity {
    Button btn_dangkysdt_xacnhan;
    EditText et_nhapsodt,et_nhapusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_kysdt__send_otp);
        setTitle("Đăng ký tài khoản bằng số điện thoại");
        btn_dangkysdt_xacnhan = (Button) findViewById(R.id.btn_dangky_sdt_xacnhan);
        et_nhapsodt = (EditText) findViewById(R.id.et_nhapsdt);
        et_nhapusername = (EditText) findViewById(R.id.et_nhapusername);

        final ProgressBar progressBar = findViewById(R.id.progressbar);
        btn_dangkysdt_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_nhapsodt.getText().toString().trim().isEmpty()){
                    Toast.makeText(ActivityDangKysdt_SendOTP.this,"Vui lòng nhập số điện thoại",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                btn_dangkysdt_xacnhan.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                       "+84" + et_nhapsodt.getText().toString(),60, TimeUnit.SECONDS,
                        ActivityDangKysdt_SendOTP.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                btn_dangkysdt_xacnhan.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                btn_dangkysdt_xacnhan.setVisibility(View.VISIBLE);
                                Toast.makeText(ActivityDangKysdt_SendOTP.this,"Vui lòng nhập đúng số điện thoại",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String vertficationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                btn_dangkysdt_xacnhan.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(ActivityDangKysdt_SendOTP.this,ActivityDangKysdt_VerifyOTP.class);
                                intent.putExtra("phone","+84"+et_nhapsodt.getText().toString());
                                intent.putExtra("vertficationId",vertficationId);
                                //intent.putExtra("username",et_nhapusername.getText().toString());
                                startActivity(intent);
                            }
                        }
                );

            }
        });
    }
}
