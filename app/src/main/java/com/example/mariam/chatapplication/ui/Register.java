package com.example.mariam.chatapplication.ui;

/**
 * Created by Domtyyyyyy on 8/8/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mariam.chatapplication.R;
import com.example.mariam.chatapplication.model.Authenticator;
import com.example.mariam.chatapplication.model.FirebaseAuthenticator;
import com.example.mariam.chatapplication.model.SignUpListener;
import com.example.mariam.chatapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.registerActivity_editText_email)
    EditText emailTextView;
    @BindView(R.id.registerActivity_editText_password)
    EditText passwordTextView;
    @BindView(R.id.registerActivity_editText_username)
    EditText usernameTextView;
    @BindView(R.id.registerActivity_button_register)
    Button registerButton;
    @BindView(R.id.registerActivity_editText_confirm_password)
    EditText confirmPasswordEditText;
    @BindView(R.id.register_show_password)
    CheckBox showPassword;
    @BindView(R.id.profile_photo)
    ImageView profilePhoto;
    private static final int GALLERY_INTENT = 2;
    ProgressDialog pd;

    String username, password, email, confirmPassword, image;
    Uri uri, downloadUri;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        registerButton.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(getString(R.string.imagePath));
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    passwordTextView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    passwordTextView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

    }


    void register() {
        username = usernameTextView.getText().toString();
        password = passwordTextView.getText().toString();
        email = emailTextView.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();

        pd = ProgressDialog.show(Register.this, null, getString(R.string.loading_msg), true);

        if (TextUtils.isEmpty(email)) {
            emailTextView.setError(getString(R.string.emailError));
            pd.dismiss();

        } else if (TextUtils.isEmpty(username)) {
            usernameTextView.setError(getString(R.string.usernameError));
            pd.dismiss();

        } else if (TextUtils.isEmpty(password)) {
            passwordTextView.setError(getString(R.string.passwordError));
            pd.dismiss();

        } else if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError(getString(R.string.confirm_error));
            pd.dismiss();

        } else {
            Authenticator authenticator = new FirebaseAuthenticator();
            authenticator.signUp(email, password, new SignUpListener() {
                @Override
                public void onSignupSuccess() {
                    if (uri != null) {
                        checkUriNotNull();

                    } else {
                        checkUriNull();
                    }
                }

                @Override
                public void onSignUpFailed(String task) {
                    if (task.equals(true)) {
                        Toast.makeText(Register.this, getString(R.string.used_email_msg), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(Register.this, R.string.register_failed, Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerActivity_button_register:
                register();
                break;
        }
    }

    void checkUriNotNull() {
        StorageReference filepath = FirebaseStorage.getInstance().getReference().child(getString(R.string.photoDb)).child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUri = taskSnapshot.getDownloadUrl();
                FirebaseDatabase.getInstance()
                        .getReference(getString(R.string.usersDb))
                        .child(auth.getCurrentUser().getUid())
                        .setValue(new User(username, downloadUri.toString(), auth.getCurrentUser().getUid()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(Register.this, UsersActivity.class);
                                pd.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });

    }

    void checkUriNull() {
        FirebaseDatabase.getInstance()
                .getReference(getString(R.string.usersDb))
                .child(auth.getCurrentUser().getUid())
                .setValue(new User(username, null, auth.getCurrentUser().getUid()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(Register.this, UsersActivity.class);
                        pd.dismiss();
                        startActivity(intent);
                        finish();
                    }
                });
    }
}