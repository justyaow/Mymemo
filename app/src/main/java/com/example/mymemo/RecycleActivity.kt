package com.example.mymemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.app.AlertDialog
import android.util.Log
import android.content.Intent

class RecycleActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter1
    private val memoList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recycle)

        recyclerView = findViewById(R.id.recyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val username = intent.getStringExtra("username")
        Log.d("aaaaaa", "rec username" + username.toString())
//        Log.d("homeLogin", "get username successful")
        adapter = MyAdapter1(this, memoList,username.toString(), ::editMemo, ::deleteMemo)
//        Log.d("homeLogin", "create adapter successful")
        recyclerView.adapter = adapter
    }

    private fun editMemo(position: Int) {
        val input = EditText(this)
        input.setText(memoList[position])
        AlertDialog.Builder(this)
            .setTitle("详情")
            .setView(input)
            .setNegativeButton("取消", null)
            .show()
    }

    private fun deleteMemo(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("删除备忘录")
            .setMessage("确定要彻底删除这条备忘录吗？")
            .setPositiveButton("删除") { _, _ ->
                adapter.removeItem(position)
            }
            .setNegativeButton("取消", null)
            .show()
    }
}