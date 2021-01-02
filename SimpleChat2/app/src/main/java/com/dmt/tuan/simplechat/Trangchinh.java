package com.dmt.tuan.simplechat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.fragment.ContactFragment;
import com.dmt.tuan.simplechat.fragment.FriendFragment;
import com.dmt.tuan.simplechat.fragment.GroupFragment;
import com.dmt.tuan.simplechat.fragment.MessageFragment;
import com.dmt.tuan.simplechat.fragment.MoreFragment;
import com.dmt.tuan.simplechat.khoa.client.ServerInfo;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Trangchinh extends AppCompatActivity {
    TextView username;
    Toolbar t_toolbar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    CircleImageView avatar;
    FloatingActionButton fabAdd;

    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchinh);

        t_toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(t_toolbar);
        getSupportActionBar().setTitle("");
        avatar = (CircleImageView) findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.username);
        fabAdd = findViewById(R.id.fabAdd);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessageFragment()).commit();

        addFriend();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nguoidung nguoidung = snapshot.getValue(Nguoidung.class);

                username.setText(nguoidung.getUsername());
                try {
                    if (nguoidung.getImageURL().equals("default")){
                        avatar.setImageResource(R.drawable.icon_thanhcong);
                    }else {
                        Glide.with(Trangchinh.this).load(nguoidung.getImageURL()).into(avatar);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addFriend();
    }

    private void addFriend() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Trangchinh.this,FindFriend.class));
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_message:
                            selectedFragment = new MessageFragment();
                            break;
                        case R.id.nav_friend:
                            selectedFragment = new FriendFragment();
                            break;
                        case R.id.nav_contact:
                            selectedFragment = new ContactFragment();
                            break;
                        case R.id.nav_group:
                            selectedFragment = new GroupFragment();
                            break;
                        case R.id.nav_more:
                            SharedPreferences.Editor editor = getSharedPreferences("Users", MODE_PRIVATE).edit();
                            editor.putString("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectedFragment = new MoreFragment();
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dangxuat:
                FirebaseAuth.getInstance().signOut();
                //Can than vi app co the bi crash
                startActivity(new Intent(Trangchinh.this, Trangchu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
            case R.id.addcontact:
                startActivity(new Intent(Trangchinh.this,AddContact.class));
                return true;
        }

        return false;
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
            super.onResume();
            status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        status("offline");
    }
}
