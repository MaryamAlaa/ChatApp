package com.example.mariam.chatapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mariam.chatapplication.R;
import com.example.mariam.chatapplication.adapter.ChatMsgAdapter;
import com.example.mariam.chatapplication.model.ChatMessage;
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
 * Created by mahmoud Shaeer on 5/8/2017.
 */

public class ChatRoom extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.input)
    EditText inputTextView;
    @BindView(R.id.fab)
    ImageView fab;
    String input;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ChatMsgAdapter chatMsgAdapter;
    List<ChatMessage> chatMessageList;
    LinearLayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    ChatMessage chatMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = inputTextView.getText().toString();
                if (!input.isEmpty()) {
               //     chatMessage = new ChatMessage(getIntent().getStringExtra("username"), auth.getCurrentUser().getEmail(), input);
                    database.getReference("Messages").push().setValue(chatMessage);
                   // database.getReference().child("UsersActivity").child(auth.getCurrentUser().getUid()).setValue(chatMessage);
                    inputTextView.setText("");
                } else {
                    inputTextView.setError("enter message");
                }

            }
        });
        readMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chatroom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOut:
                signOut();
                finish();
        }
        return true;
    }

    private void signOut() {
        auth.signOut();
    }

    @Override
    public void onBackPressed() {

    }

    public void readMessage() {
        database.getReference("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, ChatMessage> map = new HashMap<>();
                chatMessageList = new ArrayList<>(map.values());
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    chatMessage = dss.getValue(ChatMessage.class);
                    chatMessageList.add(chatMessage);
//                    Log.v("user",chatMessage.getMsgUser() );
                }
                chatMsgAdapter = new ChatMsgAdapter(getApplication(), chatMessageList);
                recyclerView.setAdapter(chatMsgAdapter);
                chatMsgAdapter.notifyDataSetChanged();
                layoutManager.scrollToPosition(chatMessageList.size() - 1);
                //     Log.v("listee", chatMessageList.size() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}