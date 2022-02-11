package com.example.chatme.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed
import com.example.chatme.R
import com.example.chatme.databinding.ActivityMainBinding
import com.example.chatme.databinding.ActivitySplashBinding
import com.example.chatme.ui.fragments.AuthenticationFragment
import com.example.chatme.ui.fragments.SplashFragment
import com.example.chatme.ui.fragments.enterUserDataFragment

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var binding :ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_contaoner,SplashFragment()).commit()



    }
}