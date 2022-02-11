package com.example.chatme.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.chatme.R
import com.example.chatme.databinding.FragmentChatsBinding
import com.example.chatme.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    var dbReference= FirebaseDatabase.getInstance().reference.child("Users")
    lateinit var binding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)
        GlobalScope.launch(Dispatchers.Main) {
            dbReference.child(FirebaseAuth.getInstance().uid.toString()).get().addOnSuccessListener {
                var img=it.children.elementAt(4).value.toString()
                activity?.let { it1 -> Glide.with(it1.baseContext).load(img.toUri()).into(binding.profileImg) }
                binding.aboutProfile.text=it.children.elementAt(0).value.toString()
                binding.userNameProfile.text=it.children.elementAt(1).value.toString()
                binding.numberProfile.text=it.children.elementAt(2).value.toString()
            }
                .addOnFailureListener{
                    Log.d("tag", "onCreateView: ${it.message}")
                }
        }
        return binding.root
    }


}