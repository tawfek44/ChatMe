package com.example.chatme.classes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatme.R;
import com.example.chatme.classes.PhoneUtility;
import com.example.chatme.classes.chatClass;
import com.example.chatme.ui.ChatActivity;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {
    private ArrayList<PhoneUtility> chatElements;
    private Context context;

    public chatAdapter(ArrayList<PhoneUtility> chatElements, Context context) {
        this.chatElements = chatElements;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item_recycler,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv.setText(chatElements.get(position).getName());
        Glide.with(context).load(chatElements.get(position).getImage()).into(holder.im);
    }


    @Override
    public int getItemCount() {
        return chatElements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView im;
        private TextView tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            im=itemView.findViewById(R.id.pic_chat_item);
            tv=itemView.findViewById(R.id.name_chat_out);
            itemView.findViewById(R.id.chat_layout_out).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, ChatActivity.class);
                    i.putExtra("About",chatElements.get(getAdapterPosition()).getStatus());
                    i.putExtra("Name",chatElements.get(getAdapterPosition()).getName());
                    i.putExtra("Number",chatElements.get(getAdapterPosition()).getNumber());
                    i.putExtra("UID",chatElements.get(getAdapterPosition()).getID());
                    i.putExtra("img", chatElements.get(getAdapterPosition()).getImage());
                    context.startActivity(i);
                }
            });
        }
    }
}
