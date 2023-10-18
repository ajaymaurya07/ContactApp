package com.example.usercontactapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usercontactapp.databinding.ActivityMainBinding
import com.example.usercontactapp.databinding.UserDialogPageBinding

class MainActivity : AppCompatActivity(), PermissionCallback {
    lateinit var binding: ActivityMainBinding
    lateinit var mydb: MyDB
    lateinit var myecylerAdapter:MyecylerAdapter
    lateinit var list:ArrayList<Person>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mydb=MyDB(this)
        list=mydb.fetchdata()
        binding.recylerView.layoutManager=LinearLayoutManager(this)
        myecylerAdapter=MyecylerAdapter(list,this,this)
        binding.recylerView.adapter=myecylerAdapter

        temp()

    }


    fun recyleradpter(){
        myecylerAdapter=MyecylerAdapter(mydb.fetchdata(),this,this)
        binding.recylerView.adapter=myecylerAdapter
    }

    fun temp(){
        binding.addBtn.setOnClickListener {
            var dialog=Dialog(this)
            var view=LayoutInflater.from(this).inflate(R.layout.user_dialog_page,null)
            var dialogbinding=UserDialogPageBinding.bind(view)
            dialog.setContentView(dialogbinding.root)
            dialog.show()

            var layoutManager=WindowManager.LayoutParams()
            layoutManager.width=WindowManager.LayoutParams.MATCH_PARENT
            layoutManager.height=WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes=layoutManager

//            dialog.dismiss()

            dialogbinding.btnContact.setOnClickListener {
                if(dialogbinding.firstName.editText!!.text.isNotEmpty() && dialogbinding.phoneNumber.editText!!.text.isNotEmpty()){
                    Toast.makeText(this,"${dialogbinding.firstName.editText!!.text} successfully added",Toast.LENGTH_SHORT).show()
                    mydb.inserdata(dialogbinding.firstName.editText!!.text.toString(),dialogbinding.lastName.editText!!.text.toString(),dialogbinding.phoneNumber.editText!!.text.toString())
                    recyleradpter()
                    dialog.dismiss()
                }else{
                    dialogbinding.firstName.error="mendatry feild"
                    dialogbinding.phoneNumber.error="mendatry feild"
                }

            }

        }
    }

//    override fun requestCallPermission() {
//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),1001)
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode==1001 && grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
////            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
////            startActivity(intent)
////            how to take value of phone number
//
//        }
//        else{
//
//        }
//    }

    var temp=0
    override fun requestCallPermission(position: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            makecall(position)
        } else {
            // You don't have the permission, request it
            temp=position
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1001)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can make a phone call
                if(temp!=0){
                    makecall(temp)
                }

            } else {
                // Permission denied, handle this case (e.g., show a message)
                // You may want to inform the user that the call cannot be made without the permission.
            }
        }

    }

    fun makecall(position: Int){

        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${list[position].phoneNumber}"))
        startActivity(intent)
    }


}