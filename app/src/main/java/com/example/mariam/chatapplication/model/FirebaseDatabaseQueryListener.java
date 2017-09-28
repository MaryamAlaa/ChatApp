package com.example.mariam.chatapplication.model;


import android.content.Context;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Domtyyyyyy on 8/25/2017.
 */

public  interface FirebaseDatabaseQueryListener {
    void querySuccess( DataSnapshot dataSnapshot);
    void queryFailed();
}

