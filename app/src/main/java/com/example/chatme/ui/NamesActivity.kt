package com.example.chatme.ui

import android.Manifest.permission.READ_CONTACTS
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatme.R
import com.example.chatme.classes.PhoneUtility
import com.example.chatme.classes.adapters.contactsAdapter
import com.example.chatme.databinding.ActivityNamesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.log

class NamesActivity : AppCompatActivity() {
    lateinit var binding: ActivityNamesBinding
    var arr =ArrayList<PhoneUtility>();
    var contactsMap=HashMap<String,Int>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarContact)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.drawable.back_arrow);
        supportActionBar?.setDisplayUseLogoEnabled(true);
        getPermission()
    }

    private fun getPermission() {
        if(ContextCompat.checkSelfPermission(applicationContext, READ_CONTACTS) !=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(READ_CONTACTS),100)
        }
        else{
            readNamesFromMobile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==100){
           if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
               readNamesFromMobile()
           }
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @SuppressLint("Range", "Recycle")
    private fun readNamesFromMobile() {
        var sort=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+"ASC";
        var cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
        GlobalScope.launch(Dispatchers.IO) {
            if (cursor?.count!! > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    val selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                    val phoneCursor =
                        contentResolver.query(uriPhone, null, selection, arrayOf(id), null)
                    if (phoneCursor!!.moveToNext()) {
                        var number =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        number = ArabicNumberToEnglishNumber(number)
                        if (number[0] != '+')
                            number = "+2$number"
                        if(!contactsMap.containsKey(number)) {
                            contactsMap[number] = 0
                            filterContacts(number.toString())
                        }
                    }
                }

            }
            contactsMap.clear()
            cursor.close()
        }
    }

    private fun filterContacts(number:String) {
            FirebaseDatabase.getInstance().reference.child("Users").orderByChild("Number").
            equalTo(number).addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.children.count()>0) {
                        var img=snapshot.children.elementAt(0).child("imageUrl").value
                        var number=snapshot.children.elementAt(0).child("Number").value
                        var name=snapshot.children.elementAt(0).child("Name").value
                        var status=snapshot.children.elementAt(0).child("About").value
                        val id=snapshot.children.elementAt(0).child("UID").value
                        arr.add(PhoneUtility(img.toString(),number.toString(),name.toString(),status.toString(),id.toString()))
                        GlobalScope.launch(Dispatchers.Main) {
                            showContacts()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

    private fun showContacts() {
        binding.contactsPr.visibility=View.GONE;
        binding.numContacts.text=arr.count().toString()
        val recyclerAdapter= contactsAdapter(arr,applicationContext)
        binding.contactsRecycler.layoutManager=LinearLayoutManager(applicationContext)
        binding.contactsRecycler.itemAnimator=DefaultItemAnimator()
        binding.contactsRecycler.adapter=recyclerAdapter
    }

    fun ArabicNumberToEnglishNumber(number: String): String {
        val arabicNumber = mutableListOf<String>()
        for (element in number) {
            when (element) {
                '١' -> arabicNumber.add("1")
                '٢' -> arabicNumber.add("2")
                '٣' -> arabicNumber.add("3")
                '٤' -> arabicNumber.add("4")
                '٥' -> arabicNumber.add("5")
                '٦' -> arabicNumber.add("7")
                '٧' -> arabicNumber.add("7")
                '٨' -> arabicNumber.add("8")
                '٩' -> arabicNumber.add("9")
                '٠' -> arabicNumber.add("0")
                else -> arabicNumber.add(element.toString())
            }
        }
        return arabicNumber.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
            .replace(" ", "").trim()


    }

}

