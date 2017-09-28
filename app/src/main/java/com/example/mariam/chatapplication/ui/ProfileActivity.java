package com.example.mariam.chatapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.mariam.chatapplication.R;
import com.example.mariam.chatapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements Serializable {
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.useremail)
    TextView useremail;
    @BindView(R.id.userphoto)
    RoundedImageView userphoto;
    FirebaseDatabase database;
    Uri uri;
    FirebaseAuth auth;
    public static final int GALLERY_INTENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        User user = (User) getIntent().getExtras().getSerializable("profile");
        username.setText(user.getMsgUser());
        useremail.setText(auth.getCurrentUser().getEmail());
        if (user.getUserImage() != null) {
            Picasso.with(ProfileActivity.this)
                    .load(user.getUserImage())
                    .fit()
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.drawable.progress_img)
                    .into(userphoto);
        }

        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(getString(R.string.imagePath));
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            StorageReference filepath = FirebaseStorage.getInstance()
                    .getReference()
                    .child(getString(R.string.photoDb))
                    .child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("userImage").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       Picasso.with(ProfileActivity.this).load(downloadUri.toString()).fit().into(userphoto);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
    }
}
