package com.dmt.tuan.simplechat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.ActivityChat;
import com.dmt.tuan.simplechat.R;
import com.dmt.tuan.simplechat.model.Chat;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_User extends RecyclerView.Adapter<Adapter_User.ViewHolder> {

    private Context context;
    private List<Nguoidung> nguoidungs;
    private boolean isChat;

    String theLastMessage;

    public Adapter_User(Context context, List<Nguoidung> nguoidungs,boolean isChat) {

        this.context = context;
        this.nguoidungs = nguoidungs;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public Adapter_User.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new Adapter_User.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Nguoidung nguoidung = nguoidungs.get(position);
        holder.username.setText(nguoidung.getUsername());

            if ("default".equals(nguoidung.getImageURL())){
                holder.image_user.setImageResource(R.drawable.icon_thanhcong);
            }else {
                Picasso.get().load(nguoidung.getImageURL()).into(holder.image_user);
            }

        if (isChat){
            lastMessage(nguoidung.getId(),holder.last_message);
        }else {
            holder.last_message.setVisibility(View.GONE);
        }

        if (isChat){
            if (nguoidung.getStatus().equals("online")){
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            }else {
                holder.image_on.setVisibility(View.GONE);
                holder.image_off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.image_on.setVisibility(View.GONE);
            holder.image_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChat.class);
                intent.putExtra("userid",nguoidung.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nguoidungs.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public TextView username;
        public ImageView image_user;
        private ImageView image_on;
        private ImageView image_off;
        private TextView last_message;
        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.chat_username);
            image_user = itemView.findViewById(R.id.image_user);
            image_on = itemView.findViewById(R.id.image_on);
            image_off = itemView.findViewById(R.id.image_off);
            last_message = itemView.findViewById(R.id.last_message);
        }

    }

    private void lastMessage(final String userid, final TextView last_message){
        theLastMessage = "default";
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    try {
                        if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)
                                || chat.getReceiver().equals(userid) && chat.getSender().equals(fuser.getUid())){
                            theLastMessage = chat.getMessage();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                switch (theLastMessage){
                    case "default":
                        last_message.setText("Không có tin nhắn");
                        break;
                        default:
                            last_message.setText(theLastMessage);
                            break;
                }
                //theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
