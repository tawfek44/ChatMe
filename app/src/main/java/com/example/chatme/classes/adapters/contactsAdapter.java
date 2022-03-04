package com.example.chatme.classes.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatme.R;
import com.example.chatme.classes.PhoneUtility;
import com.example.chatme.ui.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class contactsAdapter extends RecyclerView.Adapter<contactsAdapter.ViewModel> {
    private ArrayList<PhoneUtility>contactList=new ArrayList<>();
    private Context context;
    public contactsAdapter(ArrayList<PhoneUtility>contactList, Context context)
    {
        this.contactList=contactList;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewModel(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_item_phones,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModel holder, int position) {
        Glide.with(context).load(contactList.get(position).getImage()).into(holder.img);
        holder.tvContactName.setText(contactList.get(position).getName());
        holder.tvContactStatus.setText(contactList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvContactName,tvContactStatus;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            img=(ImageView) itemView.findViewById(R.id.contact_img);
            tvContactName=(TextView) itemView.findViewById(R.id.contact_user_name);
            tvContactStatus=(TextView) itemView.findViewById(R.id.contact_status);
            itemView.findViewById(R.id.contact_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, ChatActivity.class);
                    i.putExtra("About",contactList.get(getAdapterPosition()).getStatus());
                    i.putExtra("Name",contactList.get(getAdapterPosition()).getName());
                    i.putExtra("Number",contactList.get(getAdapterPosition()).getNumber());
                    i.putExtra("UID",contactList.get(getAdapterPosition()).getID());
                    i.putExtra("img", contactList.get(getAdapterPosition()).getImage());
                    context.startActivity(i);
                }
            });
        }
    }
}
