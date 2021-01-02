package com.dmt.tuan.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.tuan.simplechat.khoa.client.ServerSignUp;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ActivityDangky_Email extends AppCompatActivity {
    EditText et_nhapemail,et_nhappassword,et_nhaprepassword,et_nhapten;
    Button btn_dangky_email_xacnhan;
    ProgressBar progressBar_email;
    FirebaseAuth auth;
    TextView tv_dangky_checkemail;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatabaseReference reference;

    public static final Pattern VALIDATE_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky__email);
        setTitle("Đăng ký tài khoản bằng Email");
        et_nhapemail = (EditText) findViewById(R.id.et_nhapemail);
        btn_dangky_email_xacnhan = (Button) findViewById(R.id.btn_dangky_email_xacnhan);
        progressBar_email = (ProgressBar) findViewById(R.id.progressbar_email);

        et_nhapten = (EditText) findViewById(R.id.et_nhapten);
        et_nhappassword = (EditText) findViewById(R.id.et_nhappassword);
        et_nhaprepassword = (EditText) findViewById(R.id.et_nhaprepassword);

        tv_dangky_checkemail = (TextView) findViewById(R.id.tv_dangky_checkemail);

        auth  = FirebaseAuth.getInstance();
        auth.setLanguageCode("vi-VN");

        et_nhapemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_dangky_checkemail.setVisibility(View.VISIBLE);
                tv_dangky_checkemail.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_dangky_checkemail.setVisibility(View.VISIBLE);
                if (et_nhapemail.getText().toString().trim().matches(emailPattern) && s.length() > 0){
                    tv_dangky_checkemail.setText("Email hợp lệ");
                    tv_dangky_checkemail.setTextColor(Color.parseColor("#28ed46"));
                }else {
                    tv_dangky_checkemail.setText("Email không hợp lệ");
                    tv_dangky_checkemail.setTextColor(Color.parseColor("#fa2c07"));
                }
            }
        });

        btn_dangky_email_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_nhapemail.getText().toString();
                String password = et_nhappassword.getText().toString();
                String repassword = et_nhaprepassword.getText().toString();

                Dangky(et_nhapten.getText().toString(),et_nhapemail.getText().toString(),et_nhappassword.getText().toString());
    }

    public void Dangky(final String ten, String email, String password){
        if (et_nhappassword.getText().toString().equalsIgnoreCase(et_nhaprepassword.getText().toString())){
            progressBar_email.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(et_nhapemail.getText().toString(),
                    et_nhappassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar_email.setVisibility(View.GONE);
                    if (task.isSuccessful()){

//                        auth.getCurrentUser().sendEmailVerification()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        progressBar_email.setVisibility(View.GONE);
//                                        if (task.isSuccessful()){
//
//                                            Toast.makeText(ActivityDangky_Email.this,"Vui lòng xác nhận Email",Toast.LENGTH_LONG).show();
//                                            et_nhapemail.setText("");
//                                            et_nhappassword.setText("");
//                                            et_nhaprepassword.setText("");
//                                            startActivity(new Intent(ActivityDangky_Email.this,Thongbao_Dangky_Thanhcong.class));
//                                        }else {
//                                            Toast.makeText(ActivityDangky_Email.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
                        auth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar_email.setVisibility(View.GONE);
                                        if (task.isSuccessful()){

                                            Toast.makeText(ActivityDangky_Email.this,"Vui lòng xác nhận Email",Toast.LENGTH_LONG).show();

                                            String userid = auth.getCurrentUser().getUid();
                                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                            HashMap<String,String> hashMap = new HashMap<String, String>();
                                            hashMap.put("id",userid);
                                            hashMap.put("username", ten);
                                            hashMap.put("imageURL","default");
                                            hashMap.put("status","offline");
                                            hashMap.put("search",ten.toLowerCase());
                                            hashMap.put("email",et_nhapemail.getText().toString());
                                            //startActivity(new Intent(ActivityDangky_Email.this,Thongbao_Dangky_Thanhcong.class));
                                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        progressBar_email.setVisibility(View.GONE);
                                                        Intent intent = new Intent(ActivityDangky_Email.this,Thongbao_Dangky_Thanhcong.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                        //DK THANH CONG
                                                        Nguoidung nguoidung = new Nguoidung(ten,
                                                                et_nhapemail.getText().toString(),
                                                                et_nhappassword.getText().toString());

                                                        ServerSignUp signIn = new ServerSignUp(nguoidung);
                                                        signIn.execute();

                                                        et_nhapemail.setText("");
                                                        et_nhappassword.setText("");
                                                        et_nhaprepassword.setText("");

                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });
                                        }else {
                                            Toast.makeText(ActivityDangky_Email.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }else {
                        Toast.makeText(ActivityDangky_Email.this,task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            Toast.makeText(ActivityDangky_Email.this,"Vui lòng nhập lại mật khẩu chính xác",Toast.LENGTH_LONG)
                    .show();
        }
    }
        });
    }
}
