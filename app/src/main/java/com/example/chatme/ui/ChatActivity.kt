package com.example.chatme.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatme.classes.MessageDetails
import com.example.chatme.classes.adapters.contactsAdapter
import com.example.chatme.classes.adapters.messageAdapter
import com.example.chatme.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    var arr=ArrayList<MessageDetails>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDataToHeadBar()
        readMessagesFromFirebase()
        binding.chatButtonSendMassege.setOnClickListener {
            if(binding.chatTextMessage.text.isNotEmpty())
            {
                sendMessageToFirebase()
                binding.chatTextMessage.text.clear()
            }
        }
    }

    private fun readMessagesFromFirebase() {
        arr.clear()
        GlobalScope.launch (Dispatchers.IO){
            val postListner = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val mID = it.children.elementAt(1).value
                        val mText = it.children.elementAt(0).value
                        val mTime = it.children.elementAt(2).value
                        val sID = it.children.elementAt(3).value
                        arr.add(MessageDetails(mText.toString(), mID.toString(), mTime.toString(), sID.toString()))

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
            FirebaseAuth.getInstance().uid?.let {
                intent.getStringExtra("UID")?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("ChatList").child(it).child(it1)
                        .addValueEventListener(postListner)
                }
            }
        }
        GlobalScope.launch (Dispatchers.Main) {
            showMessagesToUser(arr)
            arr.clear()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessagesToUser(arr1:ArrayList<MessageDetails>) {
        val recyclerAdapter= messageAdapter(arr1,applicationContext)
        binding.messagesRecycler.layoutManager= LinearLayoutManager(applicationContext)
        binding.messagesRecycler.adapter=recyclerAdapter
        val xx=(binding.messagesRecycler.adapter?.itemCount)
        if (xx != null) {
            if(xx>0){
                binding.messagesRecycler.scrollToPosition(xx-1)
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessageToFirebase() {
        val localTime: LocalTime = LocalTime.now()
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val x=HashMap<String,String>();
        x["Message"]=binding.chatTextMessage.text.toString();
        x["MessageTime"]=localTime.format(dateTimeFormatter);
        x["SenderID"]=FirebaseAuth.getInstance().uid.toString()
        x["MessageID"]=FirebaseDatabase.getInstance().reference.push().key.toString();
        arr.add(MessageDetails(x["Message"],x["MessageID"],x["MessageTime"],x["SenderID"]))
        FirebaseAuth.getInstance().uid?.let {
            FirebaseDatabase.getInstance().reference.child("ChatList").child(it).child(intent.getStringExtra("UID").toString())
                .child(x["MessageID"].toString()).setValue(x)
        }

        FirebaseAuth.getInstance().uid?.let {
            FirebaseDatabase.getInstance().reference.child("ChatList").child(intent.getStringExtra("UID").toString()).child(it)
                .child(x["MessageID"].toString()).setValue(x)
        }

        showMessagesToUser(arr)
        arr.clear()
    }

    private fun setDataToHeadBar() {
        Glide.with(applicationContext).load(intent.getStringExtra("img")?.toUri()).into(binding.chatImage)
        binding.chatUserName.text=intent.getStringExtra("Name")
    }
}