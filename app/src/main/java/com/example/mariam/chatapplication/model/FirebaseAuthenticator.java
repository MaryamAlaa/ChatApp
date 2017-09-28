package com.example.mariam.chatapplication.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Domtyyyyyy on 8/24/2017.
 */

public class FirebaseAuthenticator implements Authenticator {
    FirebaseAuth auth;

    public FirebaseAuthenticator() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(String email, String password, final SignInListener listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSignInSuccess();
                } else {
                    listener.onSignInFailed();
                }
            }
        });
    }

    @Override
    public void signUp(String email, String password, final SignUpListener listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSignupSuccess();

                } else {
                    listener.onSignUpFailed(String.valueOf(task.getException().getMessage().equals("Email is used before")));

                }

            }
        });
    }

    @Override
    public void signOut() {
        auth.signOut();
    }
}
