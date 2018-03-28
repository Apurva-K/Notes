package com.android.notes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import java.util.*

/**
 * Created by Apurva on 27-03-2018.
 *
 * Class to handle database operation
 */
class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1)
{

    companion object
    {
        private var instance: MyDatabaseOpenHelper? = null
        var tableName = "Notes"
        lateinit var sqlObj: SQLiteDatabase
        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper
        {
            if (instance == null)
            {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            sqlObj = instance!!.writableDatabase
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(tableName, true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "text" to TEXT, "title" to TEXT, "date" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(tableName, true)
    }

    //Insert values in database
    fun insertValues(values: ContentValues): Long {
        return sqlObj.insert(tableName, "", values)
    }

    //get Inserted values
    fun fetchNotes(): ArrayList<Map<String, Any?>> {
        val hashMapArrayList = ArrayList<Map<String, Any?>>()
        sqlObj.select(tableName).exec {
            parseList(object : MapRowParser<Map<String, Any?>> {
                override fun parseRow(columns: Map<String, Any?>): Map<String, Any?> {
                    hashMapArrayList.add(columns)
                    return columns
                }
            })
        }
        return hashMapArrayList
    }

    //Delete one row as date is unique so date wise
    fun delete(date:String):Int
    {
        return sqlObj.delete(tableName, "date = '$date'")
    }
}
//To make database accessible outside class
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)
