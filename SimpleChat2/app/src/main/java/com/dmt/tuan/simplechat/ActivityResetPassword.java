package com.dmt.tuan.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityResetPassword extends AppCompatActivity {
    EditText et_nhapemail;
    Button btn_reset;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quên mật khẩu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_reset = findViewById(R.id.btn_resetpassword);

        et_nhapemail = findViewById(R.id.et_nhapemail);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_nhapemail.getText().toString();
                if (email.equals("")){
                    Toast.makeText(ActivityResetPassword.this,"Bạn chưa nhập địa chỉ email",Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ActivityResetPassword.this,"Bạn vui lòng kiểm tra email",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ActivityResetPassword.this,Dangnhap_TaiKhoan.class));
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ActivityResetPassword.this,error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
