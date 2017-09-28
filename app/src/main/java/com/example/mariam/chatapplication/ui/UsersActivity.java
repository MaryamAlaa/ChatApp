package com.example.mariam.chatapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mariam.chatapplication.R;
import com.example.mariam.chatapplication.adapter.UsersAdapter;
import com.example.mariam.chatapplication.model.FirebasDatabase;
import com.example.mariam.chatapplication.model.FirebaseDatabaseQueryListener;
import com.example.mariam.chatapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Domtyyyyyy on 8/20/2017.
 */

public class UsersActivity extends AppCompatActivity {
    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;
    FirebaseAuth auth;
    UsersAdapter usersAdapter;
    LinearLayoutManager layoutManager;
    User user, myUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);
        layoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(layoutManager);
        auth = FirebaseAuth.getInstance();
        FirebasDatabase firebasDatabase = new FirebasDatabase();
        firebasDatabase.query( new FirebaseDatabaseQueryListener() {
            @Override
            public void querySuccess( DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<User>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user = child.getValue(User.class);
                    if (user.getUserId().equals(auth.getCurrentUser().getUid())) {
                        myUser = user;
                    }
                    users.add(user);
                }
                usersAdapter = new UsersAdapter(users, UsersActivity.this);
                usersRecyclerView.setAdapter(usersAdapter);
            }



            @Override
            public void queryFailed() {
            }
        });

    }

    @Override
    public void onBackPressed() {
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
            case R.id.profile:
                showProfile();
        }
        return true;
    }

    private void signOut() {
        auth.signOut();
    }

    private void showProfile() {
        Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);
        intent.putExtra("profile", myUser);
        startActivity(intent);
    }
}
