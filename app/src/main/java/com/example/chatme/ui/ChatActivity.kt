package com.example.chatme.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatme.classes.MessageDetails
import com.example.chatme.classes.adapters.messageAdapter
import com.example.chatme.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.elementAt
import kotlin.collections.forEach
import kotlin.collections.set


class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
     private lateinit var uri: Uri
    var storageReference: StorageReference =FirebaseStorage.getInstance().reference.child("chatImages")
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
        binding.sendImageButton.setOnClickListener{
            getImageFromMobile()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadImageToFireStorage(imgUri:Uri) {
        val localTime: LocalTime = LocalTime.now()
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val xx=HashMap<String,String>();
        xx["MessageTime"]=localTime.format(dateTimeFormatter);
        xx["SenderID"]=FirebaseAuth.getInstance().uid.toString()
        xx["MessageID"]=FirebaseDatabase.getInstance().reference.push().key.toString();
        xx["MessageType"]="image"
        val imgName=storageReference.child("image"+imgUri.lastPathSegment)
        imgName.putFile(imgUri).addOnSuccessListener {
            imgName.downloadUrl.addOnCompleteListener {
                xx["Message"]=it.result.toString();
                FirebaseAuth.getInstance().uid?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("ChatList").child(it1).
                    child(intent.getStringExtra("UID").toString()).child(xx["MessageID"].toString())
                        .setValue(xx)
                }


                FirebaseAuth.getInstance().uid?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("ChatList").
                    child(intent.getStringExtra("UID").toString()).
                    child(it1).child(xx["MessageID"].toString())
                        .setValue(xx)
                }

            }
        }

    }

    private fun getImageFromMobile() {
        val intent=Intent();
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100 && resultCode== RESULT_OK && data!=null && data.data!=null){
            uri = data?.data!!
            uploadImageToFireStorage(uri)

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
                        val mType = it.children.elementAt(3).value
                        val sID = it.children.elementAt(4).value
                        /////////////////////////
                        arr.add(MessageDetails(mText.toString(), mID.toString(), mTime.toString(), sID.toString(),mType.toString()))

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
        x["MessageType"]="text"
        arr.add(MessageDetails(x["Message"],x["MessageID"],x["MessageTime"],x["SenderID"],x["MessageType"]))
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