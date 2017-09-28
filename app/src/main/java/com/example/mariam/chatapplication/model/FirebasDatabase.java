package com.example.mariam.chatapplication.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Domtyyyyyy on 8/24/2017.
 */

public class FirebasDatabase {
    FirebaseDatabase database;
    FirebaseAuth auth;


    public FirebasDatabase() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void query(final FirebaseDatabaseQueryListener listener) {
// USersActivity
        database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.querySuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.queryFailed();
            }
        });

    }

    public void insert(final FirebaseDatabaseInsertListener listener) {

    }

    public void delete() {
    }
}