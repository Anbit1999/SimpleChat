package com.dmt.tuan.simplechat.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.ActivityProfile;
import com.dmt.tuan.simplechat.Adapter.Adapter_Friend;
import com.dmt.tuan.simplechat.Adapter.Adapter_User;
import com.dmt.tuan.simplechat.R;
import com.dmt.tuan.simplechat.khoa.client.ServerFriendManager;
import com.dmt.tuan.simplechat.khoa.client.ServerInfo;
import com.dmt.tuan.simplechat.model.ChatList;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.util.concurrent.atomic.AtomicReference;


public class FriendFragment extends Fragment {
    private RecyclerView recyclerView;

    private Adapter_User adapter_user;
    private Adapter_Friend adapter_friend;
    private List<Nguoidung> nguoidungs;
    DatabaseReference reference;

    EditText search_friend;

    DatabaseReference referenceContacts,referenceUsers;
    FirebaseAuth auth;
    String currentUserID;
    private String CURRENT_STATE;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        recyclerView = view.findViewById(R.id.recycle_listfriend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nguoidungs = new ArrayList<>();
        //readUsers();
        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        referenceContacts = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        referenceUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        //reference= FirebaseDatabase.getInstance().getReference().child("Request");
        search_friend = view.findViewById(R.id.search_friend);
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
        return view;
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
                    if (!nguoidung.getId().equals(fuser.getUid())){
                        nguoidungs.add(nguoidung);
                    }
                }
                adapter_friend = new Adapter_Friend(getContext(),nguoidungs,false);
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
                adapter_user = new Adapter_User(getContext(), nguoidungs, true);
                recyclerView.setAdapter(adapter_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    List<String> friends = new ArrayList<String>();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Nguoidung>()
                .setQuery(referenceContacts,Nguoidung.class).build();

        FirebaseRecyclerAdapter<Nguoidung, ContactsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Nguoidung, ContactsViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Nguoidung model) {
                        final String userID = getRef(position).getKey();

                        referenceUsers.child(userID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("imageURL")){

                                    Log.d("messs", "DI VAO");
                                    String id = snapshot.child("id").getValue().toString();
                                    ServerInfo.nguoidung.addFriend(id);

//                                    Log.d("messs", friends.add(id)+"");
                                    Log.d("messs", id);

                                    ServerFriendManager friendManager = new ServerFriendManager(ServerInfo.nguoidung);
                                    friendManager.execute();

                                    String imageURL = snapshot.child("imageURL").getValue().toString();
                                    String userName = snapshot.child("username").getValue().toString();
                                    String search = snapshot.child("search").getValue().toString();
                                    String status = snapshot.child("status").getValue().toString();

                                    holder.contact_username.setText(userName);
                                    try {
                                        Glide.with(getContext()).load(imageURL).into(holder.image_user);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else {
                                    String imageURL = snapshot.child("imageURL").getValue().toString();
                                    String userName = snapshot.child("username").getValue().toString();
                                    String search = snapshot.child("search").getValue().toString();
                                    String status = snapshot.child("status").getValue().toString();
                                    holder.contact_username.setText(userName);
                                    try {
                                        holder.image_user.setImageResource(R.drawable.icon_thanhcong);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), ActivityProfile.class);
                                intent.putExtra("userid", userID);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,parent,false);
                        ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                        return viewHolder;
                    }


                };


//        ServerInfo.nguoidung.setEmail("nmhung31051999@gmail.com");
//        Log.d("messs", ServerInfo.nguoidung.getFriends().isEmpty()+"");



        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder
    {
        TextView contact_username, contact_phone;
        ImageView image_user;


        public ContactsViewHolder(View itemView) {
            super(itemView);
            contact_username = itemView.findViewById(R.id.chat_username);
            image_user = itemView.findViewById(R.id.image_user);
        }
    }
}
