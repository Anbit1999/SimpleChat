package com.dmt.tuan.simplechat.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.ActivityProfile;
import com.dmt.tuan.simplechat.Adapter.Adapter_Contact;
import com.dmt.tuan.simplechat.Adapter.Adapter_User;
import com.dmt.tuan.simplechat.R;
import com.dmt.tuan.simplechat.model.Contact;
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
import java.util.Collections;
import java.util.List;

import static android.content.Intent.ACTION_CALL;


public class ContactFragment extends Fragment implements Adapter_Contact.OnSelectedListener{
    RecyclerView recyclerView;
    Button btn_call;
    Adapter_Contact adapter_contact;
    private List<Contact> contacts;
    String number;

    ArrayAdapter arrayAdapter;
    ArrayList<String> list;

    DatabaseReference referenceContacts,referenceUsers;
    FirebaseAuth auth;
    String currentUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_listcontact);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btn_call = view.findViewById(R.id.btn_call);
        contacts = new ArrayList<>();
        list = new ArrayList<>();

//        auth = FirebaseAuth.getInstance();
//        currentUserID = auth.getCurrentUser().getUid();
//        referenceContacts = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
//        referenceUsers = FirebaseDatabase.getInstance().getReference().child("Users");


        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},100);
        }else {
            readContact();
        }


        return view;


    }

    private void searchPhonebook(final String s) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search").startAt(s).endAt(s + "\uf8ff");
        adapter_contact = new Adapter_Contact(getContext(),contacts,this);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contacts.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //Nguoidung nguoidung = snapshot1.getValue(Nguoidung.class);
                    Contact contact = snapshot1.getValue(Contact.class);
                    assert contact != null;
                    assert fuser != null;
                    if (!contact.getUsername().equals(s)) {
                        contacts.add(contact);
                    }
                }
                //adapter_contact = new Adapter_Contact(getContext(), contacts, this);

                recyclerView.setAdapter(adapter_contact);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readContact(){
    ContentResolver contentResolver = getContext().getContentResolver();
    adapter_contact = new Adapter_Contact(getContext(),contacts,this);
    Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
    //int n  = cursor.getCount();
    if (cursor.getCount() > 0){
//        do {
//            //list.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
//            contacts.add(new Contact(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//                    ,cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                    ,cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID))));
//        }while (cursor.moveToNext());
        while (cursor.moveToNext()){
            String username = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String img = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
            contacts.add(new Contact(username,phone,img));
            recyclerView.setAdapter(adapter_contact);

        }
        cursor.close();

        adapter_contact.notifyDataSetChanged();
    }


}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        readContact();
        if (requestCode == 1){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else {
                Toast.makeText(getContext(),"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makePhoneCall() {
        if (number.trim().length()>0){
            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}
                        ,1);
            }else {
                String dial = "tel: " + number;
                startActivity(new Intent(ACTION_CALL,Uri.parse(dial)));
            }
        }else {
            Toast.makeText(getContext(),"Lỗi",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void OnSelectedClick(int position) {
        adapter_contact = new Adapter_Contact(getContext(),contacts,this);
        recyclerView.setAdapter(adapter_contact);
        number = contacts.get(position).getPhone();
        Intent intent = new Intent(ACTION_CALL);
        intent.setData(Uri.parse("tel:"+number));
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}
                    ,1);
        }else {
            String dial = "tel:" + number;
            //startActivity(new Intent(ACTION_CALL,Uri.parse(dial)));
            startActivity(intent);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Nguoidung>()
//                .setQuery(referenceContacts,Nguoidung.class).build();
//
//        FirebaseRecyclerAdapter<Nguoidung, ContactsViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Nguoidung, ContactsViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Nguoidung model) {
//                        String userID = getRef(position).getKey();
//
//                        referenceUsers.child(userID).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.hasChild("imageURL")){
//                                    String imageURL = snapshot.child("imageURL").getValue().toString();
//                                    String userName = snapshot.child("username").getValue().toString();
//                                    String search = snapshot.child("search").getValue().toString();
//                                    String status = snapshot.child("status").getValue().toString();
//
//                                    holder.contact_username.setText(userName);
//                                    try {
//                                        Glide.with(getContext()).load(imageURL).into(holder.image_user);
//                                    }catch (Exception e){
//                                        e.printStackTrace();
//                                    }
//                                }else {
//                                    String imageURL = snapshot.child("imageURL").getValue().toString();
//                                    String userName = snapshot.child("username").getValue().toString();
//                                    String search = snapshot.child("search").getValue().toString();
//                                    String status = snapshot.child("status").getValue().toString();
//
//                                    holder.contact_username.setText(userName);
//                                    try {
//                                        holder.image_user.setImageResource(R.drawable.icon_thanhcong);
//                                    }catch (Exception e){
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
//                        ContactsViewHolder viewHolder = new ContactsViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//    public static class ContactsViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView contact_username, contact_phone;
//        ImageView image_user;
//        ImageButton btn_call;
//
//        public ContactsViewHolder(View itemView) {
//            super(itemView);
//            contact_username = itemView.findViewById(R.id.contact_username);
//            contact_phone = itemView.findViewById(R.id.contact_phone);
//            image_user = itemView.findViewById(R.id.image_user);
//            btn_call = itemView.findViewById(R.id.btn_call);
//        }
//    }
}
