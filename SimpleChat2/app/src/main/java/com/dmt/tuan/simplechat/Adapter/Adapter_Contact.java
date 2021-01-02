package com.dmt.tuan.simplechat.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.ActivityChat;
import com.dmt.tuan.simplechat.R;
import com.dmt.tuan.simplechat.Trangchinh;
import com.dmt.tuan.simplechat.fragment.ContactFragment;
import com.dmt.tuan.simplechat.model.Contact;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Adapter_Contact extends RecyclerView.Adapter<Adapter_Contact.ViewHolder> {
    private static final int REQUEST_CALL = 1;
    private LayoutInflater inflater;
    private Context context;
    private List<Contact> contacts;
    private OnSelectedListener onSelectedListener;


    public Adapter_Contact(Context context, List<Contact> contacts,OnSelectedListener onSelectedListener) {
        this.context = context;
        this.contacts = contacts;
        this.onSelectedListener = onSelectedListener;
    }


    @NonNull
    @Override
    public Adapter_Contact.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,onSelectedListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        TextView  contact_username,contact_phone;
        ImageView image_user;

        contact_username = holder.contact_username;
        contact_phone = holder.contact_phone;
        image_user = holder.image_user;

        //contacts = new ArrayList<Contact>();
        contact_username.setText(contacts.get(position).getUsername());
        contact_phone.setText(contacts.get(position).getPhone());

        if (contacts.get(position).getImage_user()== null){
            image_user.setImageResource(R.drawable.icon_thanhcong);
        }else {
            //Glide.with(context).load(nguoidung.getImageurl()).into(holder.image_user);
            //Glide.with(context).load(contacts.get(position).getImage_user()).into(image_user);
            Picasso.get().load(contacts.get(position).getImage_user()).into(image_user);
        }
        //image_user.setImageResource(Integer.parseInt(contacts.get(position).getImage_user()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber = holder.contact_phone.getText().toString();
                if (phonenumber.trim().length()>0){
                    String dial = "tel:"+phonenumber;
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }else {
                    Toast.makeText(context,"Chưa nhận được số điện thoại",Toast.LENGTH_SHORT);
                }
//                if (ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE)
//                        != PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(, new String[] {Manifest.permission.CALL_PHONE},1);
//                }else {
//                    String dial = "tel:"+phonenumber;
//                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
//                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView contact_username, contact_phone;
        public ImageView image_user;
        public ImageButton btn_call;
        OnSelectedListener onSelectedListener;
        public ViewHolder(View itemView,OnSelectedListener onSelectedListener) {
            super(itemView);
            contact_username = itemView.findViewById(R.id.contact_username);
            contact_phone = itemView.findViewById(R.id.contact_phone);
            image_user = itemView.findViewById(R.id.image_user);
            btn_call = itemView.findViewById(R.id.btn_call);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSelectedListener.OnSelectedClick(getAdapterPosition());
        }
    }

    public interface OnSelectedListener{
        void OnSelectedClick(int position);
    }

}
