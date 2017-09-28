package com.example.mariam.chatapplication.ui;

/**
 * Created by Domtyyyyyy on 8/8/2017.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mariam.chatapplication.R;
import com.example.mariam.chatapplication.model.Authenticator;
import com.example.mariam.chatapplication.model.FirebaseAuthenticator;
import com.example.mariam.chatapplication.model.SignInListener;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.loginActivity_button_login)
    Button loginButton;
    @BindView(R.id.loginActivity_button_register)
    Button registerButton;
    @BindView(R.id.loginActivity_editText_email)
    EditText etEmail;
    @BindView(R.id.loginActivity_editText_password)
    EditText etPassword;
    @BindView(R.id.showPassword)
    CheckBox showPassword;
    FirebaseAuth auth;
    String email, password;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        loginButton.setOnClickListener(this);

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        registerButton.setOnClickListener(this);
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, UsersActivity.class));
        }
    }

    void login() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        pd = ProgressDialog.show(LoginActivity.this, null, "Loading... ", true);
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        } else {
            Authenticator authenticator = new FirebaseAuthenticator();
            authenticator.signIn(email, password, new SignInListener() {
                @Override
                public void onSignInSuccess() {
                    Intent intent = new Intent(LoginActivity.this, UsersActivity.class);
                    startActivity(intent);
                    pd.dismiss();
                }

                @Override
                public void onSignInFailed() {
                    Toast.makeText(LoginActivity.this, "the email or password is wrong", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginActivity_button_login:
                if (isConnected(LoginActivity.this)) {
                    login();
                } else {
                    Toast.makeText(getApplication(), R.string.error_connection, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.loginActivity_button_register:
                startActivity(new Intent(LoginActivity.this, Register.class));
                break;
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}