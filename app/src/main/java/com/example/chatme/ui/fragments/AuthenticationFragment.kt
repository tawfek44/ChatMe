package com.example.chatme.ui.fragments

import android.app.Activity
import android.app.Application
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.chatme.MainActivity
import com.example.chatme.R
import com.example.chatme.databinding.ActivitySplashBinding
import com.example.chatme.databinding.FragmentAuthenticationBinding
import com.example.chatme.databinding.FragmentSplashBinding
import com.example.chatme.ui.SplashActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class AuthenticationFragment : Fragment() {

    private lateinit var binding : FragmentAuthenticationBinding
    private lateinit var number :String
    private lateinit var storedVerificationId: String
    private lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    private val auth = FirebaseAuth.getInstance()
    private var fragment = VerificationFragment()

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
        binding = FragmentAuthenticationBinding.inflate(inflater,container,false)
        var view : View =binding.root

        binding.floatingActionButton1.setOnClickListener(View.OnClickListener {
            if(checkNumber())
            {
                binding.loadingIcon.visibility=View.VISIBLE
                sendVerificationCode((binding.countryPicker.selectedCountryCodeWithPlus+number).trim())

            }
        })
        return view
    }


    private fun checkNumber() : Boolean
    {
        number = binding.phoneNumber.text.toString()
        if(binding.phoneNumber.text.isEmpty())
        {
            binding.phoneNumber.error = "Field Is Required !"
            return false
        }
       else if(number.length<10)
        {
            binding.phoneNumber.error = "Invalid Number !"
            return false
        }
        else
        {
            binding.phoneNumber.error = null
            return true
        }
    }

    private fun sendVerificationCode(phoneNumber: String)
    {
         val option=PhoneAuthOptions.newBuilder(auth)
             .setPhoneNumber(phoneNumber)
             .setTimeout(10L,TimeUnit.SECONDS)
             .setActivity(activity as SplashActivity)
             .setCallbacks(getCallBack())
             .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }
    private fun getCallBack():PhoneAuthProvider.OnVerificationStateChangedCallbacks
    {
         var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                Log.d("tag", "onVerificationCompleted: ")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context ,e.message,Toast.LENGTH_LONG).show()
                binding.loadingIcon.visibility=View.GONE
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                var bundle =Bundle()
                bundle.putString("verificationID",storedVerificationId)
                bundle.putString("token", resendToken.toString())

                fragment.arguments = bundle
                binding.loadingIcon.visibility=View.GONE
                fragmentManager?.beginTransaction()?.replace(R.id.fragment_contaoner,fragment)?.commit()


            }
        }
        return callbacks
    }

}


