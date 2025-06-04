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

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private val memoList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val username = intent.getStringExtra("username")
        Log.d("aaaaaa", "home username: " + username.toString())
        Log.d("homeLogin", "get username successful")
        adapter = MyAdapter(this, memoList,username.toString(), ::editMemo, ::deleteMemo)
        Log.d("homeLogin", "create adapter successful")
        recyclerView.adapter = adapter

        val createButton: Button = findViewById(R.id.createButton)
        createButton.setOnClickListener {
            showCreateMemoDialog()
        }

        val recycleButton: Button = findViewById(R.id.recycleButton)
        recycleButton.setOnClickListener {
            showRecycleActivity()
        }

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            backLogin()
        }
    }

    private fun backLogin() {
        finish()
    }

    private fun showRecycleActivity() {
        val intent1 = Intent(this, RecycleActivity::class.java)
        val username = intent.getStringExtra("username")
        intent1.putExtra("username", username)
        startActivity(intent1)
    }

    private fun showCreateMemoDialog() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("新建备忘录")
            .setView(input)
            .setPositiveButton("创建") { _, _ ->
                val memoText = input.text.toString()
                if (memoText.isNotEmpty()) {
                    adapter.addItem(memoText)
                } else {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun editMemo(position: Int) {
        val input = EditText(this)
        input.setText(memoList[position])
        AlertDialog.Builder(this)
            .setTitle("编辑备忘录")
            .setView(input)
            .setPositiveButton("保存") { _, _ ->
                val newText = input.text.toString()
                if (newText.isNotEmpty()) {
                    adapter.editItem(position, newText)
                } else {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun deleteMemo(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("删除备忘录")
            .setMessage("确定要删除这条备忘录吗？")
            .setPositiveButton("删除") { _, _ ->
                adapter.removeItem(position)
            }
            .setNegativeButton("取消", null)
            .show()
    }
}