package com.example.chatme.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatme.R
import com.example.chatme.classes.PhoneUtility
import com.example.chatme.classes.adapters.chatAdapter
import com.example.chatme.classes.chatClass
import com.example.chatme.databinding.FragmentChatsBinding
import com.example.chatme.ui.NamesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatsFragment : Fragment() {
    lateinit var binding:FragmentChatsBinding
    var chatElement=ArrayList<PhoneUtility>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentChatsBinding.inflate(layoutInflater,container,false)
        getChats()
        binding.goToNames.setOnClickListener{
            startActivity(Intent(activity?.baseContext,NamesActivity::class.java))
        }
        return binding.root
    }

    private fun getChats() {
        GlobalScope.launch(Dispatchers.IO) {
            val chatListner= object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                   snapshot.children.forEach{
                       val vID=it.children.elementAt(0).key.toString();
                       if(vID!=FirebaseAuth.getInstance().uid)
                            getUserInfo(vID)
                   }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
            FirebaseDatabase.getInstance().reference.child("ChatList").addValueEventListener(chatListner);
        }
}
    fun getUserInfo(fID: String) {
        GlobalScope.launch (Dispatchers.IO){
            val chaListener2 = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        if (it.key == fID) {
                            val about=it.children.elementAt(0).value.toString()
                            val name=it.children.elementAt(1).value.toString()
                            val num=it.children.elementAt(2).value.toString()
                            val image=it.children.elementAt(4).value.toString()
                            val chatClassObject = PhoneUtility(image,num,name,about,fID)
                            chatElement.add(chatClassObject)
                            showToUser(chatElement)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
            FirebaseDatabase.getInstance().reference.child("Users")
                .addValueEventListener(chaListener2)
        }
    }

    private fun showToUser(chatElement: ArrayList<PhoneUtility>) {
        val adapter= chatAdapter(chatElement,context)
        binding.chatsRecView.layoutManager=LinearLayoutManager(context)
        binding.chatsRecView.adapter=adapter


    }
}