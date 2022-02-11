package com.example.chatme.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import com.example.chatme.R
import com.example.chatme.ui.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    private var currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_splash, container, false)
        Handler().postDelayed(3000)
        {
            Log.d("tag", "onCreateView: $currentUser")
            if(currentUser==null) fragmentManager?.beginTransaction()?.replace(R.id.fragment_contaoner,AuthenticationFragment())?.commit()
            else {
                startActivity(Intent(activity?.applicationContext, HomeActivity::class.java))
                activity?.finish()
            }
        }

        return view
    }


}