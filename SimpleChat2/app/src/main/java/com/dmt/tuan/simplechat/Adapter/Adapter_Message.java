package com.dmt.tuan.simplechat.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.ActivityChat;
import com.dmt.tuan.simplechat.ImageViewActivity;
import com.dmt.tuan.simplechat.R;
import com.dmt.tuan.simplechat.Trangchinh;
import com.dmt.tuan.simplechat.model.Chat;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Message extends RecyclerView.Adapter<Adapter_Message.ViewHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Chat> chats;
    private String image_url;

    FirebaseUser user;

    public Adapter_Message(Context context, List<Chat> chats, String image_url) {

        this.context = context;
        this.chats = chats;
        this.image_url = image_url;
    }

    @NonNull
    @Override
    public Adapter_Message.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new Adapter_Message.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new Adapter_Message.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Chat chat = chats.get(position);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if ("image".equals(chat.getType())){
            holder.showmessage.setVisibility(View.GONE);
            holder.showpicture.setVisibility(View.VISIBLE);
            Picasso.get().load(chat.getMessage()).into(holder.showpicture);
        }else if ("text".equals(chat.getType())){
            holder.showmessage.setVisibility(View.VISIBLE);
            holder.showpicture.setVisibility(View.GONE);
            holder.showmessage.setText(chat.getMessage());
        }else if ("pdf".equals(chat.getType()) || "docx".equals(chat.getType())){
            holder.showpicture.setVisibility(View.VISIBLE);
            holder.showmessage.setVisibility(View.GONE);
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/simplechatotp.appspot.com/o/Image%20Files%2Fic_filetxt.png?alt=media&token=d912bff8-aff4-48bb-8fd3-239089f29620")
                    .into(holder.showpicture);
        }
        if (chats.get(position).getSender().equals(user.getUid())){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("pdf".equals(chats.get(position).getType()) || "docx".equals(chats.get(position).getType())){
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Xóa tin nhắn chỉ mình tôi",
                                        "Tải và xem tài liệu",
                                        "Hủy",
                                        "Xóa tin nhắn tất cả mọi người"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Xóa tin nhắn");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    deleteSentMessage(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), Trangchinh.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }else if (which == 1){
                                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(chats.get(position).getMessage()));
                                    holder.itemView.getContext().startActivity(intent);
                                }else if (which == 2){

                                }else if (which == 3){
                                    deleteMessageForEveryone(position,holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), Trangchinh.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }else if ("text".equals(chat.getType())){
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Xóa tin nhắn chỉ mình tôi",
                                        "Hủy",
                                        "Xóa tin nhắn tất cả mọi người"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Xóa tin nhắn");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    deleteSentMessage(position,holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), Trangchinh.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }else if (which == 2){

                                }
                            }
                        });
                        builder.show();
                    }else if ("image".equals(chat.getType())){
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Xóa tin nhắn chỉ mình tôi",
                                        "Hiển thị ảnh toàn màn hình",
                                        "Hủy",
                                        "Xóa tin nhắn tất cả mọi người"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Xóa tin nhắn");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    deleteSentMessage(position,holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), Trangchinh.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }else if (which == 1){
                                    Intent intent = new Intent(context, ImageViewActivity.class);
                                    intent.putExtra("url",chats.get(position).getMessage());
                                    context.startActivity(intent);
                                }else if (which == 3){
                                    deleteMessageForEveryone(position,holder);
                                }
                            }
                        });
                        builder.show();
                    }
                }
            });
        }
        else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("pdf".equals(chats.get(position).getType()) || "docx".equals(chats.get(position).getType())) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Xóa tin nhắn chỉ mình tôi",
                                        "Tải và xem tài liệu",
                                        "Hủy",
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Xóa tin nhắn");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    deleteReceiveMessage(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), Trangchinh.class);
                                    holder.itemView.getContext().startActivity(intent);
                                } else if (which == 1) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(chats.get(position).getMessage()));
                                    holder.itemView.getContext().startActivity(intent);
                                } else if (which == 2) {

                                } else if (which == 3) {
                                    deleteMessageForEveryone(position, holder);
                                }
                            }
                        });
                        builder.show();
                    } else if ("text".equals(chat.getType())) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Xóa tin nhắn chỉ mình tôi",
                                        "Hủy",
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Xóa tin nhắn");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    deleteReceiveMessage(position, holder);
                                    Intent intent = new Intent(context, Trangchinh.class);
                                    holder.itemView.getContext().startActivity(intent);
                                } else if (which == 1) {

                                }
                            }
                        });
                        builder.show();
                    } else if ("image".equals(chat.getType())) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Xóa tin nhắn chỉ mình tôi",
                                        "Hiển thị ảnh toàn màn hình",
                                        "Hủy",
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Xóa tin nhắn");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    deleteReceiveMessage(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), Trangchinh.class);
                                    holder.itemView.getContext().startActivity(intent);
                                } else if (which == 1) {
                                    Intent intent = new Intent(context, ImageViewActivity.class);
                                    intent.putExtra("url", chats.get(position).getMessage());
                                    context.startActivity(intent);
                                } else if (which == 2) {
                                    dialog.dismiss();
                                }
                            }
                        });
                        builder.show();
                    }
                }
            });
        }

        try {
            if ("default".equals(image_url)){
                holder.image_user.setImageResource(R.drawable.icon_thanhcong);
            }else {
                Picasso.get().load(image_url).into(holder.image_user);
            }
        }catch (Exception e){

        }


        if (position == chats.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Đã xem");
            }else {
                holder.txt_seen.setText("Đã nhận");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

//    private void deleteSentMessage(final int position, final ViewHolder holder){
//        DatabaseReference roorRef = FirebaseDatabase.getInstance().getReference();
//        roorRef.child("ChatList").child(chats.get(position).getSender()).child(chats.get(position).getReceiver())
//            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(holder.itemView.getContext(), "Xóa tin nhắn thành công",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(holder.itemView.getContext(), "Lỗi",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
    private void deleteSentMessage(final int position, final ViewHolder holder){
        final DatabaseReference roorRef = FirebaseDatabase.getInstance().getReference();
        roorRef.child("Chats").child(chats.get(position).getSender()).child(chats.get(position).getReceiver())
                .child(chats.get(position).getType())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(holder.itemView.getContext(), "Xóa tin nhắn thành công",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(holder.itemView.getContext(), "Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deleteReceiveMessage(final int position, final ViewHolder holder){
        final DatabaseReference roorRef = FirebaseDatabase.getInstance().getReference();
        roorRef.child("Chats").child(chats.get(position).getReceiver()).child(chats.get(position).getSender())
                .child(chats.get(position).getType())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    roorRef.child("Chats").child(chats.get(position).getSender()).child(chats.get(position).getReceiver())
                            .child(chats.get(position).getType())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(holder.itemView.getContext(), "Xóa tin nhắn thành công",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(holder.itemView.getContext(), "Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteMessageForEveryone(final int position, final ViewHolder holder){
        final DatabaseReference roorRef = FirebaseDatabase.getInstance().getReference();
        roorRef.child("Chats").child(chats.get(position).getReceiver()).child(chats.get(position).getSender())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    roorRef.child("Chats").child(chats.get(position).getSender()).child(chats.get(position).getReceiver())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(holder.itemView.getContext(), "Xóa tin nhắn thành công",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(holder.itemView.getContext(), "Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public TextView showmessage;
        public ImageView image_user;
        public ImageView showpicture;

        public TextView txt_seen;
        public ViewHolder(View itemView){
            super(itemView);
            showmessage = itemView.findViewById(R.id.showmessage);
            image_user = itemView.findViewById(R.id.image_user);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            showpicture = itemView.findViewById(R.id.showpicture);
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(user.getUid())){
            return MSG_TYPE_LEFT;
        }else {
            return MSG_TYPE_RIGHT;
        }
    }
}
