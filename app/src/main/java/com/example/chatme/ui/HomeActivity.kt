package com.example.chatme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import com.example.chatme.R
import com.example.chatme.databinding.ActivityHomeBinding
import com.example.chatme.databinding.ActivitySplashBinding
import com.example.chatme.ui.fragments.CallsFragment
import com.example.chatme.ui.fragments.ChatsFragment
import com.example.chatme.ui.fragments.ProfileFragment
import com.example.chatme.ui.fragments.SplashFragment

class HomeActivity : AppCompatActivity() {
    lateinit var binding:ActivityHomeBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navBar.get(0).isSelected=true
        supportFragmentManager.beginTransaction().replace(R.id.container_fr, ChatsFragment()).commit()
        binding.navBar.setOnItemSelectedListener {
            if(it==binding.navBar.get(0).id)
            {
                supportFragmentManager.beginTransaction().replace(R.id.container_fr, ChatsFragment()).commit()
            }
            else if(it==binding.navBar.get(1).id)
            {
                supportFragmentManager.beginTransaction().replace(R.id.container_fr, CallsFragment()).commit()
            }
            else
            {
                supportFragmentManager.beginTransaction().replace(R.id.container_fr, ProfileFragment()).commit()
            }
        }

    }
}