package com.example.mariam.chatapplication.model;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Domtyyyyyy on 8/24/2017.
 */

public  interface FirebaseDatabaseInsertListener {
    void insertSuccess();
    void insertFailed();
}
