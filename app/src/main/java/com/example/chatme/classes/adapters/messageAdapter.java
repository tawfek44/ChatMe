package com.example.chatme.classes.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatme.R;
import com.example.chatme.classes.MessageDetails;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter{
    private ArrayList<MessageDetails> message;
    private Context context;
    public messageAdapter(ArrayList<MessageDetails> message,Context context)
    {
        this.message=new ArrayList<>();
        this.context=context;
        this.message=message;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder view;
        if(viewType==0)
            view = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.message_me_item_layout,parent,false));
        else if(viewType == 1)
            view = new SecondViewHolder(LayoutInflater.from(context).inflate(R.layout.message_him_item_layout,parent,false));
        else
            view = new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.image_message_layout,parent,false));
        return  view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==0) {
           MyViewHolder v= (MyViewHolder) holder;
           v.messageText.setText(message.get(position).getMessageText());
           v.messageTime.setText(message.get(position).getMessageTime());
        }
        else if(holder.getItemViewType()==1)
        {
            SecondViewHolder v2= (SecondViewHolder)holder;
            v2.messageT.setText(message.get(position).getMessageText());
            v2.messageTi.setText(message.get(position).getMessageTime());
        }
        else if(holder.getItemViewType()==11){
            ImageViewHolder2 ivh=(ImageViewHolder2)holder;
            Glide.with(context).load(Uri.parse(message.get(position).getMessageText())).into(ivh.im2);
            ((ImageViewHolder2) holder).tv2.setText(message.get(position).getMessageTime());
        }
        else{
            ImageViewHolder ivh=(ImageViewHolder)holder;
            Glide.with(context).load(Uri.parse(message.get(position).getMessageText())).into(ivh.im);
            ((ImageViewHolder) holder).tv1.setText(message.get(position).getMessageTime());
        }
    }

    @Override
    public int getItemCount() {
        return message.size();
    }
    @Override
    public int getItemViewType(int position) {
        int res=1000;
        if(message.get(position).getSenderID().equals(FirebaseAuth.getInstance().getUid())) {
            if(message.get(position).getMessageType().equals("text"))
                res= 0;
            else
                 res = -1; // images from me
        }
        else if(!message.get(position).getSenderID().equals(FirebaseAuth.getInstance().getUid())){
            if(message.get(position).getMessageText().equals("text"))
            res = 1;
            else
                res = 11; // images not from me
        }
        return res;

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText,messageTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText=itemView.findViewById(R.id.message_text_item_layout);
            messageTime=itemView.findViewById(R.id.message_time);
        }
    }

    public class SecondViewHolder extends RecyclerView.ViewHolder {
        private TextView messageT,messageTi;
        public SecondViewHolder(@NonNull View itemView) {
            super(itemView);
            messageT=itemView.findViewById(R.id.message_text_item_layout2);
            messageTi=itemView.findViewById(R.id.message_time2);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView im;
        private TextView tv1;
        private ImageView imTick1;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            im=itemView.findViewById(R.id.image_message);
            tv1=itemView.findViewById(R.id.image_message_time);
            imTick1=itemView.findViewById(R.id.image_tick);
        }
    }


    public class ImageViewHolder2 extends RecyclerView.ViewHolder {
        private ImageView im2;
        private TextView tv2;
        private ImageView imTick2;
        public ImageViewHolder2(@NonNull View itemView) {
            super(itemView);
            im2=itemView.findViewById(R.id.image_message2);
            tv2=itemView.findViewById(R.id.image_message_time2);
            imTick2=itemView.findViewById(R.id.image_tick2);
        }
    }
}
