package com.example.chatme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import com.example.chatme.R
import com.example.chatme.databinding.ActivityNamesBinding

class NamesActivity : AppCompatActivity() {
    lateinit var binding: ActivityNamesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarContact)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.drawable.back_arrow);
        supportActionBar?.setDisplayUseLogoEnabled(true);
    }


}