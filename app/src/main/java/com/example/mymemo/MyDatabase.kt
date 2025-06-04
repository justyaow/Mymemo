package com.example.mymemo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDatabase (
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("dbLogin", "init")
        db?.execSQL("PRAGMA foreign_keys = ON")
        Log.d("dbLogin", "foreign")
        db?.execSQL("create table if not exists user (username varchar(255) primary key, password varchar(255) not null)")
        db?.execSQL("create table if not exists item (id int, content TEXT not null, username varchar(255) not null, primary key(id, username), foreign key(username) references user(username) on delete cascade)")
        Log.d("dbLogin", "create successful")
        db?.execSQL("create table if not exists recycler (rid int, rcontent TEXT not null, rusername varchar(255) not null, primary key(rid, rusername), foreign key(rusername) references user(username) on delete cascade)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS user")
        db?.execSQL("DROP TABLE IF EXISTS item")
        db?.execSQL("DROP TABLE IF EXISTS recycler")
        onCreate(db)
    }
}