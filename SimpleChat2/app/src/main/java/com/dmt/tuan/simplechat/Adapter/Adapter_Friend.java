package com.dmt.tuan.simplechat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.ActivityChat;
import com.dmt.tuan.simplechat.ActivityProfile;
import com.dmt.tuan.simplechat.R;
import com.dmt.tuan.simplechat.model.Chat;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Friend extends RecyclerView.Adapter<Adapter_Friend.ViewHolder> {

    private Context context;
    private List<Nguoidung> nguoidungs;
    private boolean isChat;

    private String theLastMessage;
    private String sender_user_id;
    String receiver_user_id;

    private String current_state;
    private DatabaseReference friendRequestReference;
    private FirebaseAuth auth;

    private FirebaseUser fuser;
    public Adapter_Friend(Context context, List<Nguoidung> nguoidungs, boolean isChat) {
        this.context = context;
        this.nguoidungs = nguoidungs;
        this.isChat = isChat;




    }

    @NonNull
    @Override
    public Adapter_Friend.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        return new Adapter_Friend.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Adapter_Friend.ViewHolder holder, final int position) {
        final Nguoidung nguoidung = nguoidungs.get(position);

        holder.username.setText(nguoidung.getUsername());
            if ("default".equals(nguoidung.getImageURL())) {
                holder.image_user.setImageResource(R.drawable.icon_thanhcong);
                //Picasso.get().load(R.drawable.icon_thanhcong).into(holder.image_user);
            } else {
                Picasso.get().load(nguoidung.getImageURL()).into(holder.image_user);
            }



        if (isChat) {
            if (nguoidung.getStatus().equals("online")) {
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            } else {
                holder.image_on.setVisibility(View.GONE);
                holder.image_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.image_on.setVisibility(View.GONE);
            holder.image_off.setVisibility(View.GONE);
        }

        friendRequestReference = FirebaseDatabase.getInstance().getReference().child("Friend_Request");

        auth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        current_state = "not_friends";
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(context, ActivityChat.class);
//                intent.putExtra("userid", nguoidung.getId());
//                context.startActivity(intent);
//                receiver_user_id = nguoidungs.get(position).getId();
//
//
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ActivityProfile.class);
                intent.putExtra("userid", nguoidung.getId());
                context.startActivity(intent);

            }
        });

//        holder.btn_addfriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (current_state.equals("not_friends")){
//                    friendRequestReference.child(fuser.getUid()).child(receiver_user_id).child("request_type")
//                            .setValue("sent")
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        friendRequestReference.child(receiver_user_id).child(fuser.getUid())
//                                                .child("request_type").setValue("received")
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//                                                        Toast.makeText(context,"Đã gửi yêu cầu kết bạn",Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//                                    }else {
//                                        Toast.makeText(context,"Gửi yêu cầu kết bạn thất bại",Toast.LENGTH_LONG).show();
//                                    }
//
//                                }
//                            });
//                }
//
//                    }
//
//        });
    }


    @Override
    public int getItemCount() {
        return nguoidungs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView image_user;
        private ImageView image_on;
        private ImageView image_off;
//        private Button btn_addfriend;
//        private Button btn_cancelfriend;
//        private Button btn_acceptfriend;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.chat_username);
            image_user = itemView.findViewById(R.id.image_user);
            image_on = itemView.findViewById(R.id.image_on);
            image_off = itemView.findViewById(R.id.image_off);
//            btn_addfriend = itemView.findViewById(R.id.btn_addfriend);
//            btn_cancelfriend = itemView.findViewById(R.id.btn_cancelfriend);
//            btn_acceptfriend = itemView.findViewById(R.id.btn_acceptfriend);
        }

    }

}
