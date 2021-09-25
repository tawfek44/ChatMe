package com.example.chatme.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chatme.R
import com.example.chatme.databinding.FragmentVerificationBinding
import com.example.chatme.ui.HomeActivity
import com.example.chatme.ui.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerificationFragment : Fragment() {

   private  lateinit var binding:FragmentVerificationBinding
   private  var auth : FirebaseAuth= FirebaseAuth.getInstance()
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
        binding = FragmentVerificationBinding.inflate(layoutInflater,container,false)
        var view:View= binding.root;
        binding.floatingActionButton2.setOnClickListener()
        {
            if(checkCodeSyntax(binding.codeNumber.toString().trim()))
            {
                binding.loading.visibility=View.VISIBLE
                var id = arguments?.getString("verificationID").toString()
                checkOTP(id)
            }
        }


        return view
    }
     private fun checkCodeSyntax(code:String):Boolean
     {
         return if(code.trim().length<6) {
             binding.codeNumber.error="Invalid-Code!"
             false
         } else if(code.isEmpty()) {
             binding.codeNumber.error="Field is required!"
             false
         } else {
             binding.codeNumber.error=null
             true
         }
     }
    private fun checkOTP(id:String)
    {
        val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(id, binding.codeNumber.text.toString()
        )
        signInWithPhoneAuthCredential(credential)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                         val user = task.result?.user
                    binding.loading.visibility=View.GONE
                    startActivity(Intent(activity?.baseContext ,HomeActivity::class.java))

                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(activity?.baseContext,
                            (task.exception as FirebaseAuthInvalidCredentialsException).message,Toast.LENGTH_LONG).show()
                    }
                    // Update UI
                }
            }
    }


}