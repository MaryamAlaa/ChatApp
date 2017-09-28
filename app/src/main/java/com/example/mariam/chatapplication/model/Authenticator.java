package com.example.mariam.chatapplication.model;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Domtyyyyyy on 8/24/2017.
 */

public interface Authenticator {
    void signIn(String email, String password,SignInListener listener);
    void signUp(String email, String password, SignUpListener listener);
    void signOut();
}
