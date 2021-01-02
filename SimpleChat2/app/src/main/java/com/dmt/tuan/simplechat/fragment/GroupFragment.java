package com.dmt.tuan.simplechat.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.Adapter.Adapter_Friend;
import com.dmt.tuan.simplechat.Adapter.Adapter_User;
import com.dmt.tuan.simplechat.R;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class GroupFragment extends Fragment {
    private RecyclerView recyclerView;

    private Adapter_User adapter_user;
    private Adapter_Friend adapter_friend;
    private List<Nguoidung> nguoidungs;
    private DatabaseReference referenceRequest, referenceUsers, referenceContacts;
    private FirebaseAuth auth;
    private EditText search_friend;

    private String currentUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        referenceUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        referenceRequest = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        referenceContacts = FirebaseDatabase.getInstance().getReference().child("Contacts");


        recyclerView = view.findViewById(R.id.recycle_listfriend_request);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Nguoidung> options =
                new FirebaseRecyclerOptions.Builder<Nguoidung>()
                        .setQuery(referenceRequest.child(currentUserID), Nguoidung.class).build();

        FirebaseRecyclerAdapter<Nguoidung, RequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<Nguoidung, RequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Nguoidung model) {
                        holder.itemView.findViewById(R.id.btn_acceptrequest).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.btn_refuserequest).setVisibility(View.VISIBLE);

                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();
                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String type = snapshot.getValue().toString();

                                    if (type.equals("received")) {
                                        referenceUsers.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                if (snapshot.hasChild("imageURL")){
//                                                    String requestUserName = snapshot.child("username").getValue().toString();
//                                                    String requestSearch = snapshot.child("search").getValue().toString();
//                                                    String requestStatus = snapshot.child("status").getValue().toString();
//                                                    String requestImageUser = snapshot.child("imageURL").getValue().toString();
//
//                                                    holder.userName.setText(requestUserName);
//                                                    try {
//                                                        Glide.with(getContext()).load(requestImageUser).into(holder.image_user);
//                                                    }catch (Exception e){
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                                else {
//                                                    String requestUserName = snapshot.child("username").getValue().toString();
//                                                    String requestSearch = snapshot.child("search").getValue().toString();
//                                                    String requestStatus = snapshot.child("status").getValue().toString();
//                                                    holder.userName.setText(requestUserName);
//                                                    try {
//                                                        holder.image_user.setImageResource(R.drawable.icon_thanhcong);
//                                                    }catch (Exception e){
//                                                        e.printStackTrace();
//                                                    }
//                                                }
                                                if (snapshot.hasChild("imageURL")) {

                                                    String requestImageUser = snapshot.child("imageURL").getValue().toString();
                                                    try {
                                                        Glide.with(getContext()).load(requestImageUser).into(holder.image_user);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                final String requestUserName = snapshot.child("username").getValue().toString();
                                                String requestSearch = snapshot.child("search").getValue().toString();
                                                String requestStatus = snapshot.child("status").getValue().toString();
                                                holder.userName.setText(requestUserName);
                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        CharSequence options[] = new CharSequence[]{"Chấp nhận", "Từ chối"};

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle(requestUserName + " muốn gửi lời kết bạn");

                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (which == 0) {
                                                                    referenceContacts.child(currentUserID).child(list_user_id)
                                                                            .child(currentUserID)
                                                                            .child("Contacts")
                                                                            .setValue("Saved")
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        referenceRequest.child(currentUserID)
                                                                                                .child(list_user_id)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()){
                                                                                                            referenceRequest.child(list_user_id)
                                                                                                                    .child(currentUserID)
                                                                                                                    .removeValue()
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            if (task.isSuccessful()){
                                                                                                                                Toast.makeText(getContext(),"Đã thêm bạn bè thành công",Toast.LENGTH_LONG).show();
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });

                                                                }
                                                                if (which == 1) {
                                                                    referenceRequest.child(currentUserID)
                                                                            .child(list_user_id)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
                                                                                        referenceRequest.child(list_user_id)
                                                                                                .child(currentUserID)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()){
                                                                                                            Toast.makeText(getContext(),"Đã từ chối yêu cầu kết bạn",Toast.LENGTH_LONG).show();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                        builder.show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
                        RequestViewHolder holder = new RequestViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        CircleImageView image_user;
        Button btn_appceptrequest, btn_refuserequest;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.request_username);
            image_user = itemView.findViewById(R.id.image_user);

            btn_appceptrequest = itemView.findViewById(R.id.btn_acceptrequest);
            btn_refuserequest = itemView.findViewById(R.id.btn_refuserequest);
        }
    }
}
