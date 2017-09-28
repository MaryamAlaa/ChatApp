package com.example.mariam.chatapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mariam.chatapplication.R;
import com.example.mariam.chatapplication.adapter.ChatMsgAdapter;
import com.example.mariam.chatapplication.model.ChatMessage;
import com.example.mariam.chatapplication.model.FirebasDatabase;
import com.example.mariam.chatapplication.model.FirebaseDatabaseQueryListener;
import com.example.mariam.chatapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Domtyyyyyy on 8/20/2017.
 */

public class PrivateChat extends AppCompatActivity {
    @BindView(R.id.editText)
    EditText inputText;
    @BindView(R.id.send)
    ImageView send;
    @BindView(R.id.privateRecyclerView)
    RecyclerView recyclerView;
    String input, result;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ChatMsgAdapter chatMsgAdapter;
    LinearLayoutManager linearLayoutManager;
    ChatMessage chatMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_chat);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        database = FirebaseDatabase.getInstance();
        final User user = (User) getIntent().getExtras().getSerializable("user");
        getSupportActionBar().setTitle(user.msgUser);
        final String uid = generateUid();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = inputText.getText().toString();
                if (!input.isEmpty()) {
                    database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("userImage").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String url = dataSnapshot.getValue(String.class);
                            chatMessage = new ChatMessage(input, url);
                            database.getReference().child("Private Chat").child(uid).push().setValue(chatMessage);
                            inputText.setText("");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                } else {
                    inputText.setError("can't be blank");
                }


            }
        });

        database.getReference().child("Private Chat").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    chatMessage = dss.getValue(ChatMessage.class);
                    chatMessageList.add(chatMessage);
                }
                chatMsgAdapter = new ChatMsgAdapter(PrivateChat.this, chatMessageList);
                recyclerView.setAdapter(chatMsgAdapter);
                chatMsgAdapter.notifyDataSetChanged();
                linearLayoutManager.scrollToPosition(chatMessageList.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    String generateUid() {
        User user = (User) getIntent().getExtras().getSerializable("user");
        String idSender = auth.getCurrentUser().getUid();
        String idReceiver = user.getUserId();
        if (idSender.compareTo(idReceiver) < 0) {
            result = idSender + idReceiver;
        } else {
            result = idReceiver + idSender;
        }
        return result;
    }


}
