package com.dmt.tuan.simplechat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dmt.tuan.simplechat.Adapter.Adapter_User;
import com.dmt.tuan.simplechat.Notifications.Token;
import com.dmt.tuan.simplechat.R;
import com.dmt.tuan.simplechat.model.Chat;
import com.dmt.tuan.simplechat.model.ChatList;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MessageFragment extends Fragment {

    private RecyclerView recyclerView;

    private Adapter_User adapter_user;

    //private List<String> usersList;
    private List<ChatList> usersList;
    private List<Nguoidung> nguoidungs;
    DatabaseReference reference;
    FirebaseUser fuser;
    EditText search_chat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = view.findViewById(R.id.recycle_listchat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        usersList = new ArrayList<>();
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.requestFocus();
        //reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    ChatList chatList =   snapshot1.getValue(ChatList.class);
                    //Chat chat = snapshot1.getValue(Chat.class);
//                    if (chat.getSender().equals(fuser.getUid())){
//                        usersList.add(chat.getReceiver());
//                    }
//                    if (chat.getReceiver().equals(fuser.getUid())){
//                        usersList.add(chat.getSender());
//                    }
                    usersList.add(chatList);
                }
                //readChats();
                //listnguoidung();
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        search_chat = view.findViewById(R.id.search_chat);
        search_chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchChat(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void chatList() {
         nguoidungs = new ArrayList<>();
         reference = FirebaseDatabase.getInstance().getReference("Users");
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 nguoidungs.clear();
                 for (DataSnapshot snapshot1 : snapshot.getChildren()){
                     Nguoidung nguoidung = snapshot1.getValue(Nguoidung.class);
                     for (ChatList chatList : usersList){
                         if (chatList.getId().equals(nguoidung.getId())){
                             nguoidungs.add(nguoidung);
                         }
                     }
                 }
                 adapter_user = new Adapter_User(getContext(),nguoidungs,true);
                 recyclerView.setAdapter(adapter_user);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token);
    }
    private void listnguoidung() {
        usersList = new ArrayList<>();
        nguoidungs = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nguoidungs.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren() ){
                    Nguoidung nguoidung = new Nguoidung();
//                    for (ChatList chatList: usersList){
//                        if (nguoidung.getId().equals(chatList.getId())){
//                            nguoidungs.add(nguoidung);
//                        }
//                    }
                }
                adapter_user = new Adapter_User(getContext(),nguoidungs,true);
                recyclerView.setAdapter(adapter_user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchChat(String s) {
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
                adapter_user = new Adapter_User(getContext(), nguoidungs, true);
                recyclerView.setAdapter(adapter_user);
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
