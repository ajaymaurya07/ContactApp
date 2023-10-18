package com.example.usercontactapp

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.usercontactapp.databinding.UserDialogPageBinding
import com.google.android.material.textfield.TextInputLayout


class MyecylerAdapter(
    var list:ArrayList<Person>,
    var context: Context,
    val permissionCallback: PermissionCallback
):RecyclerView.Adapter<MyecylerAdapter.myviewHolder>() {
    var rowId=0
    inner  class myviewHolder(item:View):RecyclerView.ViewHolder(item){
        var temp=item.findViewById<LinearLayout>(R.id.call_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewHolder {
        var myview=LayoutInflater.from(parent.context).inflate(R.layout.contact_prototype,parent,false)

        return myviewHolder(myview)
    }



    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: myviewHolder, position: Int) {

        var data = list[position]
        rowId=data.id
        holder.temp.findViewById<TextView>(R.id.text_name).text = data.name
        holder.temp.findViewById<TextView>(R.id.text_phoneno).text = data.phoneNumber
        var number=holder.temp.findViewById<TextView>(R.id.text_phoneno).text.toString()

        var phoneIcon = holder.temp.findViewById<ImageView>(R.id.phone_icon)
        phoneIcon.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.CALL_PHONE
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
//                startActivity(context,intent,null)
//
//
//            } else {
//                permissionCallback.requestCallPermission()
//            }

            permissionCallback.requestCallPermission(position)

        }

        var threedot=holder.temp.findViewById<ImageView>(R.id.threedot_icon)

        threedot.setOnClickListener {
            showpopupmenu(it,holder.temp.context,holder.temp,position,holder)
        }

    }

    fun showpopupmenu(view: View,context: Context,layout: LinearLayout,position: Int,holder: myviewHolder){
        var popupMenu=PopupMenu(context,view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.clear_text->{
                    deleterow(position)
                }
                R.id.update_text->{
                    updatedata(layout,holder,position)

                }
            }
            true
        }
    }


    var mydb=MyDB(context)

    fun updatedata(layout: LinearLayout,holder: myviewHolder,position: Int){
        var dialog=Dialog(context)
        var view=LayoutInflater.from(context).inflate(R.layout.user_dialog_page,null)
        var dialogbinding= UserDialogPageBinding.bind(view)
        dialog.setContentView(dialogbinding.root)
        dialog.show()

        var layoutManager= WindowManager.LayoutParams()
        layoutManager.width= WindowManager.LayoutParams.MATCH_PARENT
        layoutManager.height= WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes=layoutManager

        var name=layout.findViewById<TextView>(R.id.text_name).text.toString()
        var phone=layout.findViewById<TextView>(R.id.text_phoneno).text.toString()
        var fullname=name.split(" ")
        var f_name=fullname[0]
        var l_name=fullname[1]
        var set_fname=dialog.findViewById<TextInputLayout>(R.id.first_name)
        var set_lname=dialog.findViewById<TextInputLayout>(R.id.last_name)
        var set_phoneno=dialog.findViewById<TextInputLayout>(R.id.phone_number)
        set_fname.editText!!.setText(f_name)
        set_lname.editText!!.setText(l_name)
        set_phoneno.editText!!.setText(phone)
        var dialogbtn=dialog.findViewById<Button>(R.id.btn_contact)
        dialogbtn.setText("Update Contact")

//        update data base using function

        dialogbinding.btnContact.setOnClickListener {
            if(dialogbinding.firstName.editText!!.text.isNotEmpty() && dialogbinding.phoneNumber.editText!!.text.isNotEmpty()){

                mydb.updatedatabase((list[position].id),dialogbinding.firstName.editText!!.text.toString(),dialogbinding.lastName.editText!!.text.toString(),dialogbinding.phoneNumber.editText!!.text.toString())
//                recyleradpter()

                holder.temp.findViewById<TextView>(R.id.text_name).setText("${dialogbinding.firstName.editText!!.text} ${dialogbinding.lastName.editText!!.text}")
                holder.temp.findViewById<TextView>(R.id.text_phoneno).setText("${dialogbinding.phoneNumber.editText!!.text}")
//                Toast.makeText(context,"$rowId",Toast.LENGTH_SHORT).show()



                dialog.dismiss()
            }else{
                dialogbinding.firstName.error="mendatry feild"
                dialogbinding.phoneNumber.error="mendatry feild"
            }

        }

    }


    fun deleterow(position: Int){
        mydb.delete((list[position].id))
        list.removeAt(position)
        notifyItemRemoved(position)
    }

}