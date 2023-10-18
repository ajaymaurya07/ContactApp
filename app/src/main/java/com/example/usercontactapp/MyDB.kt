package com.example.usercontactapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDB(var context: Context) {
    var DBNAME="contactdb"
    var DBVERSION=2
    var TABLENAME="person_detail"
    var ID="_id"
    var FNAME="_fname"
    var LNAME="_lname"
    var PHONENO="_phoneno"

    var TABLE="CREATE TABLE $TABLENAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT ,$FNAME TEXT, $LNAME TEXT,$PHONENO TEXT)"

    inner class MysqliteOpenhelper(context: Context):SQLiteOpenHelper(context,DBNAME,null,DBVERSION){
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(TABLE)
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("DROP TABLE IF EXISTS $TABLENAME")
            onCreate(p0)
        }

    }

    var mysqliteOpenhelper=MysqliteOpenhelper(context)
    var sqlitedatabase=mysqliteOpenhelper.writableDatabase

     fun inserdata(fname:String,
                    lname:String,
                    phone:String){
        var values=ContentValues()
         values.put(FNAME,fname)
         values.put(LNAME,lname)
         values.put(PHONENO,phone)
         var temp=sqlitedatabase.insert(TABLENAME,null,values)
    }

    fun fetchdata():ArrayList<Person>{
        var list= arrayListOf<Person>()
        var cursur=sqlitedatabase.rawQuery("SELECT * FROM $TABLENAME",null,null)

        if(cursur.count>0){
            cursur.moveToFirst()
            do {
                var id_row=cursur.getInt(0)
                var f_name=cursur.getString(1)
                var l_name=cursur.getString(2)
                var phone_no=cursur.getString(3)
                list.add(Person(id_row,"$f_name $l_name",phone_no))
            }while (cursur.moveToNext())
        }
        return list
    }

    fun updatedatabase(
        id:Int,
        fname:String,
                   lname:String,
                   phone:String){
        var values=ContentValues()
        values.put(FNAME,fname)
        values.put(LNAME,lname)
        values.put(PHONENO,phone)

        var rowupdate=sqlitedatabase.update(TABLENAME,values,"$ID=$id",null)
    }

    fun delete(rowid:Int){
        var rowdel=sqlitedatabase.delete(TABLENAME,"$ID=$rowid",null)
    }

}