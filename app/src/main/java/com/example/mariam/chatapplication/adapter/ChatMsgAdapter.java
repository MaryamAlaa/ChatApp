package com.example.mariam.chatapplication.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mariam.chatapplication.model.ChatMessage;
import com.example.mariam.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud Shaeer on 5/9/2017.
 */

public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ChatHolder> {
    Context c;
    List<ChatMessage> list;
    FirebaseAuth auth;

    public ChatMsgAdapter(Context c, List<ChatMessage> list) {
        this.c = c;
        this.list = list;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_area, parent, false);
        ChatHolder holder = new ChatHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        ChatMessage chatMessage = list.get(position);
        holder.userMag.setText(chatMessage.getMsgText());
        if (chatMessage.getUserImage() != null) {
            Picasso.with(c).load(chatMessage.getUserImage()).fit().into(holder.userPhoto);
        } else {
            Picasso.with(c).load(R.mipmap.ic_launcher).fit().into(holder.userPhoto);
        }
        holder.time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getTime()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userrMsg)
        TextView userMag;
        @BindView(R.id.userPhoto)
        RoundedImageView userPhoto;
        @BindView(R.id.time)
        TextView time;

        public ChatHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}