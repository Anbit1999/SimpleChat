package com.dmt.tuan.simplechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.Adapter.Adapter_Message;
import com.dmt.tuan.simplechat.Notifications.Client;
import com.dmt.tuan.simplechat.Notifications.Data;
import com.dmt.tuan.simplechat.Notifications.MyResponse;
import com.dmt.tuan.simplechat.Notifications.Sender;
import com.dmt.tuan.simplechat.Notifications.Token;
import com.dmt.tuan.simplechat.fragment.APIService;
import com.dmt.tuan.simplechat.model.Chat;
import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChat extends AppCompatActivity {
    CircleImageView avatar;
    TextView username;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;

    ImageButton btn_send, btn_openfile;
    EditText input_chat;

    Adapter_Message adapter_message;
    List<Chat> chats;

    String userid;

    ValueEventListener seenListener;
    RecyclerView recyclerView;

    String checker = "", myUrl="";
    StorageTask uploadTask;
    Uri fileUri;

    APIService apiService;

    boolean notify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityChat.this,Trangchinh.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        //final String userid = getIntent().getStringExtra("userid");
        avatar = findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.username);
        userid = getIntent().getStringExtra("userid");
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference  = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        btn_send = (ImageButton) findViewById(R.id.btn_send);
        btn_openfile = (ImageButton) findViewById(R.id.btn_openfile);
        input_chat = (EditText) findViewById(R.id.input_chat);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_viewchat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        btn_openfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{"Hình ảnh","Tệp PDF","Tệp Word"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityChat.this);
                builder.setTitle("Chọn hình thức");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            checker = "image";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent,"Chọn ảnh"),6969);

                        }
                        if (which == 1){
                            checker = "pdf";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent,"Chọn tệp PDF"),6969);
                        }
                        if (which == 2){
                            checker = "docx";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(intent.createChooser(intent,"Chọn tệp Microsoft Word"),6969);
                        }
                    }
                });
                builder.show();
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nguoidung nguoidung = snapshot.getValue(Nguoidung.class);
                username.setText(nguoidung.getUsername());
                try {
                    if (nguoidung.getImageURL().equals("default")){
                    avatar.setImageResource(R.drawable.icon_thanhcong);
                }else {
                    Glide.with(getApplicationContext()).load(nguoidung.getImageURL()).into(avatar);
                }
                }catch (Exception e){
                    e.printStackTrace();
                }
                readMessage(user.getUid(),userid,nguoidung.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        seenMessage(userid);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = input_chat.getText().toString();
                if (message.equals("")){
                    input_chat.requestFocus();
                }else {
                    sendMessage(user.getUid(),userid,message);

                }
                input_chat.setText("");
            }
        });
    }

    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    if (chat.getReceiver().equals(user.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot1.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendMessage(String sender, final String receiver, String message){
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> map = new HashMap<>();
        map.put("sender",sender);
        map.put("receiver",receiver);
        map.put("message",message);
        map.put("isseen",false);
        map.put("type","text");
        reference.child("Chats").push().setValue(map);

        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid())
                .child(userid);

        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatref.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nguoidung nguoidung = snapshot.getValue(Nguoidung.class);
                if (notify){
                    sendNotification(receiver, nguoidung.getUsername(),msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        //Token token = snapshot1.getValue(Token.class);
                        Token token = new Token();
                        Data data = new Data(user.getUid(),R.drawable.icon_thanhcong,username+": "+message, "Tin nhắn mới",userid);
                        Sender sender = new Sender(data, token.getToken());

                        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() ==  200){
                                    if (response.body().success != 1){
                                        Toast.makeText(ActivityChat.this,"Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(final String myid, final String userid, final String imageurl){
        chats = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid)
                    || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        chats.add(chat);
                    }
                    adapter_message= new Adapter_Message(ActivityChat.this, chats,imageurl);
                    recyclerView.setAdapter(adapter_message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6969 && resultCode == RESULT_OK && data!= null && data.getData()!=null){
            fileUri = data.getData();
            final ProgressDialog progressDialog = new ProgressDialog(ActivityChat.this);
            progressDialog.setTitle("Đang gửi ảnh");
            progressDialog.setMessage("Chờ giây lát. Ảnh đang được tải lên");
            //progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            if (!checker.equals("image")){
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Document Files");
                final DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();

                final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid())
                        .child(userid);

                String messagePushID = chatref.getKey();
                final StorageReference filePath = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(fileUri));
                filePath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("sender",user.getUid());
                                map.put("receiver",userid);
                                map.put("message", downloadUrl.toString());
                                map.put("name",fileUri.getLastPathSegment());
                                map.put("type",checker);
                                map.put("isseen",false);
                                reference.child("Chats").push().setValue(map);

                                final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid())
                                        .child(userid);

                                progressDialog.dismiss();
                                chatref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()){
                                            chatref.child("id").setValue(userid);
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"Đã tải tệp thành công",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityChat.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p  = (100.0*snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Đã tải lên được: " + (int) p +" %");
                    }
                });
            }else if (checker.equals("image")){
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");
                final DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();

                final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid())
                        .child(userid);

                String messagePushID = chatref.getKey();

                //final StorageReference filePath = storageReference.child(user.getUid() + "." + "jpg");
                final StorageReference filePath = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(fileUri));
                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();

                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("sender",user.getUid());
                            map.put("receiver",userid);
                            map.put("message",myUrl);
                            map.put("name",fileUri.getLastPathSegment());
                            map.put("type",checker);
                            map.put("isseen",false);
                            reference.child("Chats").push().setValue(map);

                            final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid())
                                    .child(userid);
                            progressDialog.dismiss();
                            chatref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()){
                                        chatref.child("id").setValue(userid);
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Đã tải ảnh thành công",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });

            }else {
                progressDialog.dismiss();
                Toast.makeText(this,"Không có ảnh được chọn",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }
}
