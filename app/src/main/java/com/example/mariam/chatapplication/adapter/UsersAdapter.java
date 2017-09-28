package com.example.mariam.chatapplication.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mariam.chatapplication.ui.PrivateChat;
import com.example.mariam.chatapplication.R;
import com.example.mariam.chatapplication.model.User;
import com.example.mariam.chatapplication.ui.UsersActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Domtyyyyyy on 8/20/2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.userHolder> {
    List<User> usersList;
    Context context;
    ProgressDialog pd;

    public UsersAdapter(List<User> usersList, UsersActivity context) {
        this.usersList = usersList;
        this.context = context;
    }

    @Override
    public userHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        UsersAdapter.userHolder holder = new UsersAdapter.userHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(userHolder holder, int position) {
        final User user = usersList.get(position);
        holder.userName.setText(user.getMsgUser());
        if (user.getUserImage() != null) {
            Picasso.with(context)
                    .load(user.getUserImage())
                    .fit()
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.drawable.progress_img)
                    .into(holder.userImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PrivateChat.class);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class userHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userImage)
        com.makeramen.roundedimageview.RoundedImageView userImage;


        public userHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
