package com.dmt.tuan.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmt.tuan.simplechat.fragment.MoreFragment;
import com.dmt.tuan.simplechat.khoa.client.ServerFriendManager;
import com.dmt.tuan.simplechat.khoa.client.ServerInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {
    String receiverUserID,senderUserID, current_state, current_user_id;

    private CircleImageView userProfileImage;
    private TextView userProfileName;
    Button btn_sendMessage,btn_sendRequest,btn_cancelRequest,btn_acceptRequest,btn_refuseRequest;

    DatabaseReference reference, referenceChatRequest,referenceContactsRequest;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        receiverUserID = getIntent().getExtras().get("userid").toString();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        referenceChatRequest = FirebaseDatabase.getInstance().getReference("Chat Requests");
        referenceContactsRequest = FirebaseDatabase.getInstance().getReference("Contacts");

        auth = FirebaseAuth.getInstance();
        senderUserID = auth.getCurrentUser().getUid();

        userProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        userProfileName = (TextView) findViewById(R.id.tv_username_profile);

        btn_sendMessage = (Button) findViewById(R.id.btn_sendmessage);
        btn_sendRequest = (Button) findViewById(R.id.btn_sendrequest);
        btn_cancelRequest = (Button) findViewById(R.id.btn_cancelrequest);
        btn_acceptRequest = (Button) findViewById(R.id.btn_acceptrequest);
        btn_refuseRequest = (Button) findViewById(R.id.btn_refuserequest);

        current_state = "new";
        RetriveUserInfo();

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProfile.this, ActivityChat.class);
                intent.putExtra("userid", receiverUserID);
                startActivity(intent);
            }
        });

    }

    private void RetriveUserInfo() {
        reference.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("imageURL")){
                    String userid = snapshot.child("id").getValue().toString();
                    String userImage = snapshot.child("imageURL").getValue().toString();
                    String userName = snapshot.child("username").getValue().toString();
                    String search = snapshot.child("search").getValue().toString();
                    String status = snapshot.child("status").getValue().toString();
                    try {
                        Glide.with(ActivityProfile.this).load(userImage).into(userProfileImage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    userProfileName.setText(userName);
                    ManageChatRequest();
                }else {
                    userProfileImage.setImageResource(R.drawable.icon_thanhcong);
                    String userName = snapshot.child("username").getValue().toString();
                    String search = snapshot.child("search").getValue().toString();
                    String status = snapshot.child("status").getValue().toString();

                    userProfileName.setText(userName);
                    ManageChatRequest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ManageChatRequest() {
        referenceChatRequest.child(senderUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(receiverUserID)){
                            String request_type = snapshot.child(receiverUserID).child("request_type").getValue().toString();
                            if (request_type.equals("sent")){
                                current_state = "request_sent";
                                btn_sendRequest.setText("Hủy yêu cầu");
                            }else if (request_type.equals("received")){
                                current_state = "request_received";
                                btn_sendRequest.setText("Chấp nhận yêu cầu");
                                btn_cancelRequest.setVisibility(View.VISIBLE);

                                btn_cancelRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CancelRequestSent();
                                    }
                                });
                            }
                        }else {
                            referenceContactsRequest.child(senderUserID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(receiverUserID)){
                                                current_state = "friends";
                                                btn_sendRequest.setText("Xóa bạn bè");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        if (!senderUserID.equals(receiverUserID)){
            btn_sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_sendRequest.setEnabled(false);
                    if (current_state.equals("new")){
                        SendchatRequest();
                    }
                    if (current_state.equals("request_sent")){
                        CancelRequestSent();
                    }
                    if (current_state.equals("request_received")){
                        AcceptRequestSend();
                    }
                    if (current_state.equals("friends")){
                        RemoveRequestSent();
                    }
                }
            });
        }else {
            btn_sendRequest.setVisibility(View.VISIBLE);
        }
    }

    private void RemoveRequestSent() {
        referenceContactsRequest.child(senderUserID).child(receiverUserID)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    referenceContactsRequest.child(receiverUserID).child(senderUserID)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                btn_sendRequest.setEnabled(true);
                                current_state = "new";
                                btn_sendRequest.setText("Gửi yêu cầu");

                                ServerInfo.nguoidung.getFriends().remove(receiverUserID);
                                ServerFriendManager friendManager = new ServerFriendManager(ServerInfo.nguoidung);
                                friendManager.execute();

                                btn_cancelRequest.setVisibility(View.GONE);
                                btn_cancelRequest.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
    }

    private void AcceptRequestSend() {
        referenceContactsRequest.child(senderUserID).child(receiverUserID)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ServerInfo.nguoidung.getFriends().add(receiverUserID);
                            ServerFriendManager friendManager = new ServerFriendManager(ServerInfo.nguoidung);
                            friendManager.execute();
                            referenceContactsRequest.child(receiverUserID).child(senderUserID)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                referenceChatRequest.child(senderUserID).child(receiverUserID)
                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        btn_sendRequest.setEnabled(true);
                                                        current_state = "friends";
                                                        btn_sendRequest.setText("Xóa bạn bè");
                                                        btn_cancelRequest.setVisibility(View.GONE);
                                                        btn_cancelRequest.setEnabled(false);


                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void CancelRequestSent() {
        referenceChatRequest.child(senderUserID).child(receiverUserID)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    referenceChatRequest.child(receiverUserID).child(senderUserID)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                btn_sendRequest.setEnabled(true);
                                current_state = "new";
                                btn_sendRequest.setText("Gửi yêu cầu");

                                btn_cancelRequest.setVisibility(View.GONE);
                                btn_cancelRequest.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
    }

    private void SendchatRequest() {
        referenceChatRequest.child(senderUserID).child(receiverUserID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        if (task.isSuccessful()){
                            referenceChatRequest.child(receiverUserID).child(senderUserID)
                                    .child("request_type").setValue("received")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (task.isSuccessful()){
                                                btn_sendRequest.setEnabled(true);
                                                current_state = "request_sent";
                                                btn_sendRequest.setText("Hủy yêu cầu");
                                                //btn_sendRequest.setText("Hủy yêu cầu");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
