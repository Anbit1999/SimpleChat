package com.dmt.tuan.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class Dangnhap_SoDienThoai_Verifyotp extends AppCompatActivity {
    EditText et_otp1, et_otp2, et_otp3, et_otp4, et_otp5, et_otp6;
    Button btn_xacnhan_OTP;
    private String verificationId;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap__so_dien_thoai__verifyotp);

        setTitle("Đăng nhập bằng số điện thoại");
        progressBar = (ProgressBar) findViewById(R.id.progressbar_verify);

        et_otp1 = (EditText) findViewById(R.id.et_otp1);
        et_otp2 = (EditText) findViewById(R.id.et_otp2);
        et_otp3 = (EditText) findViewById(R.id.et_otp3);
        et_otp4 = (EditText) findViewById(R.id.et_otp4);
        et_otp5 = (EditText) findViewById(R.id.et_otp5);
        et_otp6 = (EditText) findViewById(R.id.et_otp6);

        setupOTPInputs();
        btn_xacnhan_OTP = (Button) findViewById(R.id.btn_xacnhan_OTP);

        TextView txt_sodienthoai = findViewById(R.id.tv_sodienthoai);
        txt_sodienthoai.setText(getIntent().getStringExtra("phone"));

        verificationId = getIntent().getStringExtra("vertficationId").toString();
        btn_xacnhan_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_otp1.getText().toString().trim().isEmpty()
                        || et_otp2.getText().toString().trim().isEmpty()
                        || et_otp2.getText().toString().trim().isEmpty()
                        || et_otp2.getText().toString().trim().isEmpty()
                        || et_otp2.getText().toString().trim().isEmpty()
                        || et_otp2.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Dangnhap_SoDienThoai_Verifyotp.this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = et_otp1.getText().toString() + et_otp2.getText().toString() + et_otp3.getText().toString() +
                        et_otp4.getText().toString() + et_otp5.getText().toString() + et_otp6.getText().toString();

                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    btn_xacnhan_OTP.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId, code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    btn_xacnhan_OTP.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(Dangnhap_SoDienThoai_Verifyotp.this,Trangchinh.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Dangnhap_SoDienThoai_Verifyotp.this, "Mã OTP bạn vừa nhập không hợp lệ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void setupOTPInputs() {
        et_otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    et_otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    et_otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    et_otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    et_otp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    et_otp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
