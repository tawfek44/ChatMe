package com.example.chatme.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.chatme.R
import com.example.chatme.databinding.FragmentEnterUserDataBinding
import com.example.chatme.databinding.FragmentVerificationBinding
import com.example.chatme.ui.HomeActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.ref.PhantomReference
import java.net.URI
import kotlin.math.log

class enterUserDataFragment : Fragment() {
    private  lateinit var binding: FragmentEnterUserDataBinding
    private val IMAGE_CODE : Int=1
    lateinit var  imgUri: Uri
    var auth= FirebaseAuth.getInstance()
    var dbReference = FirebaseDatabase.getInstance().reference.child("Users")
    var stotageReference: StorageReference=FirebaseStorage.getInstance().reference.child("imageUsers")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentEnterUserDataBinding.inflate(layoutInflater,container,false)
        var view=binding.root;
        binding.getImageFromGallery1.setOnClickListener {
            getImageFromGallery()
        }
        binding.btnGoChats.setOnClickListener{
            if(binding.textInputEdittextName.text?.isNotEmpty() == true) {
                saveImageIntoFireBase(imgUri)
                binding.progressCircular.visibility = View.VISIBLE
            }else
            {
                binding.textInputEdittextName.error="Required!"
            }
        }

        return view;
    }
    private fun saveImageIntoFireBase(imgUri: Uri)
    {
     val imageName=stotageReference.child("image"+imgUri.lastPathSegment)
        imageName.putFile(imgUri).addOnSuccessListener {
            imageName.downloadUrl.addOnCompleteListener {
                val hashMap: HashMap<String, String> = HashMap()
                hashMap["UID"]=FirebaseAuth.getInstance().uid.toString()
                hashMap["Name"]=binding.textInputEdittextName.text.toString()
                if(binding.textInputEdittextAbout.text!!.isEmpty())
                    hashMap["About"]="Heey There Im Using ChatMe"
                else
                hashMap["About"]=binding.textInputEdittextAbout.text.toString()

                hashMap["imageUrl"] = it.result.toString()
                hashMap["Number"]= auth.currentUser?.phoneNumber.toString()
                dbReference.child(hashMap["UID"].toString()).setValue(hashMap)
                startActivity(Intent(activity?.baseContext,HomeActivity::class.java))
                activity?.finish()
                binding.progressCircular.visibility=View.GONE
            }
        }
    }



    private fun getImageFromGallery()
    {
        var intent= Intent()
        intent.type = "image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_CODE && resultCode==RESULT_OK && data!=null && data.data!=null)
        {
            imgUri= data.data!!
            binding.userImg1.setImageURI(data.data)
        }
    }
}