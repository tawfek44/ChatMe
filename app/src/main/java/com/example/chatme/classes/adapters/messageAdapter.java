package com.example.chatme.classes.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder view;
        if(viewType==0)
            view = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.message_me_item_layout,parent,false));
        else
            view = new SecondViewHolder(LayoutInflater.from(context).inflate(R.layout.message_him_item_layout,parent,false));
        return (MyViewHolder) view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==0) {
           MyViewHolder v= (MyViewHolder) holder;
           v.messageText.setText(message.get(position).getMessageText());
           v.messageTime.setText(message.get(position).getMessageTime());
        }
        else
        {
            SecondViewHolder v2= (SecondViewHolder)holder;
            v2.messageT.setText(message.get(position).getMessageText());
            v2.messageTi.setText(message.get(position).getMessageTime());
        }
    }

    @Override
    public int getItemCount() {
        return message.size();
    }
    @Override
    public int getItemViewType(int position) {
        if(message.get(position).getSenderID().equals(FirebaseAuth.getInstance().getUid()))
            return 0;
        else return 1;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
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
}
