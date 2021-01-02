package com.dmt.tuan.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.dmt.tuan.simplechat.Adapter.Adapter_Friend;
import com.dmt.tuan.simplechat.Adapter.Adapter_User;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriend extends AppCompatActivity {
    Toolbar t_toolbar;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    BottomNavigationView bottomNavigationView;

    RecyclerView recyclerView;

    Adapter_User adapter_user;
    Adapter_Friend adapter_friend;
    List<Nguoidung> nguoidungs;


    EditText search_friend;

    private String CURRENT_STATE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        t_toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(t_toolbar);
        getSupportActionBar().setTitle("Tìm bạn bè");

        recyclerView = findViewById(R.id.recycle_listfriend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        nguoidungs = new ArrayList<>();
        readUsers();

        //reference= FirebaseDatabase.getInstance().getReference().child("Request");
        search_friend = findViewById(R.id.search_friend);

        search_friend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFriend(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void readUsers() {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nguoidungs.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Nguoidung nguoidung = snapshot1.getValue(Nguoidung.class);
                    assert nguoidung != null;
                    assert fuser != null;
//                    if (!nguoidung.getId().equals(fuser.getUid())){
//                        nguoidungs.add(nguoidung);
//                    }
                    if (!fuser.getUid().equals(nguoidung.getId())){
                        nguoidungs.add(nguoidung);
                    }
                }
                adapter_friend = new Adapter_Friend(FindFriend.this,nguoidungs,false);
                recyclerView.setAdapter(adapter_friend);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchFriend(String s) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nguoidungs.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Nguoidung nguoidung = snapshot1.getValue(Nguoidung.class);
                    assert nguoidung != null;
                    assert fuser != null;
                    if (!nguoidung.getId().equals(fuser.getUid())) {
                        nguoidungs.add(nguoidung);
                    }
                }
                adapter_user = new Adapter_User(FindFriend.this, nguoidungs, true);
                recyclerView.setAdapter(adapter_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
