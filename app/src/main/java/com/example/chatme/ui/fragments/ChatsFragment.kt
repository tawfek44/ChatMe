package com.example.chatme.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.chatme.R
import com.example.chatme.databinding.FragmentChatsBinding
import com.example.chatme.ui.NamesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatsFragment : Fragment() {
    lateinit var binding:FragmentChatsBinding
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
        binding.goToNames.setOnClickListener{
            startActivity(Intent(activity?.baseContext,NamesActivity::class.java))
        }
        return binding.root
    }

}