package com.dmt.tuan.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.tuan.simplechat.khoa.client.ServerInfo;
import com.dmt.tuan.simplechat.khoa.client.ServerLogin;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Dangnhap_TaiKhoan extends AppCompatActivity {
    EditText et_dangnhap_taikhoan,et_matkhau_taikhoan;
    TextView tv_quenmatkhau;
    Button btn_dangnhap_taikhoan,btn_dangnhap_sdt;
    ProgressBar progressBar;

    private String email;
    private String password;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap__tai_khoan);
        setTitle("Đăng nhập");

        context = this;

        et_dangnhap_taikhoan = (EditText) findViewById(R.id.et_dangnhap_taikhoan);
        et_matkhau_taikhoan = (EditText) findViewById(R.id.et_matkhau_taikhoan);

        btn_dangnhap_taikhoan = (Button) findViewById(R.id.btn_dangnhap_taikhoan);
        btn_dangnhap_sdt = (Button) findViewById(R.id.btn_dangnhap_sdt);
        tv_quenmatkhau = (TextView) findViewById(R.id.tv_quenmatkhau);

        progressBar = (ProgressBar) findViewById(R.id.progressbar_dangnhap);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        btn_dangnhap_taikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_dangnhap_taikhoan.getText().toString()) ||
                        TextUtils.isEmpty(et_matkhau_taikhoan.getText().toString())){
                    Toast.makeText(Dangnhap_TaiKhoan.this,"Bạn cần nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                }else {

                    email = et_dangnhap_taikhoan.getText().toString();
                    password = et_matkhau_taikhoan.getText().toString();

                    auth.signInWithEmailAndPassword(et_dangnhap_taikhoan.getText().toString(),et_matkhau_taikhoan.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        if (auth.getCurrentUser().isEmailVerified()){
                                            //TIEN HANH XAC THUC QA DYNAMO

                                            Nguoidung nguoidung = new Nguoidung(email, password);
                                            ServerInfo.nguoidung = nguoidung;

                                            Log.d("messs", email);
                                            Log.d("messs", password);
                                            ServerLogin login = new ServerLogin(nguoidung);
                                            login.execute();
                                            try { Thread.sleep(1000);} catch (Exception e) {e.printStackTrace(); }
                                            String result = login.getResult();



                                            if("true".equalsIgnoreCase(result)){
                                                progressBar.setVisibility(View.VISIBLE);
                                                Intent intent = new Intent(Dangnhap_TaiKhoan.this,Trangchinh.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                et_matkhau_taikhoan.setText("");
                                                finish();
                                            }else{
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("Thông báo");
                                                builder.setMessage(result);
                                                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                });

                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        }else {
                                            Toast.makeText(Dangnhap_TaiKhoan.this,"Vui lòng xác nhận tài khoản Email của bạn",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(Dangnhap_TaiKhoan.this,"Tài khoản không tồn tại",Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                }

            }
        });

        tv_quenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dangnhap_TaiKhoan.this,ActivityResetPassword.class));
            }
        });

        btn_dangnhap_sdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dangnhap_TaiKhoan.this,DangNhap_SoDienThoai.class));
            }
        });
    }


}
