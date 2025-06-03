package com.example.mymemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.EditText
import android.content.SharedPreferences
import android.content.Intent
import android.widget.Toast
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.CheckBox

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setStatus()
    }

    fun setStatus() {
//        Log.d("main", "start")
        val prefs: SharedPreferences = getSharedPreferences("user_information", MODE_PRIVATE)
        val usernameText: EditText = findViewById(R.id.usernameText)
        val passwordText: EditText = findViewById(R.id.passwordText)
//        if (prefs.contains("lastUsername")) {
//            Log.d("mainprefs", "yes")
//        } else {
//            Log.d("mainprefs", "no")
//        }
        val username: String? = prefs.getString("lastUsername", "")
        val password: String? = prefs.getString("lastPassword", "")
//        Log.d("mainlastUseranme", username.toString())
        val checkSave: CheckBox = findViewById(R.id.checkSave)
        if (password != "") {
            checkSave.setChecked(true)
        } else {
            checkSave.setChecked(false)
        }
        usernameText.setText(username)
        passwordText.setText(password)
    }

    fun handleLogin(view: View) {
        val usernameText: EditText = findViewById(R.id.usernameText)
        val passwordText: EditText = findViewById(R.id.passwordText)
        val checkSave: CheckBox = findViewById(R.id.checkSave)
        val username: String = usernameText.text.toString()
        val password: String = passwordText.text.toString()
        val prefs: SharedPreferences = getSharedPreferences("user_information", MODE_PRIVATE)
        if (username == "" || password == "") {
            Toast.makeText(this, "请填写完整", Toast.LENGTH_SHORT).show()
            return
        }
        val userInfo = MyDatabase(this, "memo", null, 1)
        val infoW: SQLiteDatabase = userInfo.writableDatabase
        val infoR: SQLiteDatabase = userInfo.readableDatabase
        val cursor = infoR.rawQuery("select password from user where username=?", arrayOf(username))
        if (cursor.moveToFirst()) {
//            val p: String? = prefs.getString(username, "")
//            if (p != password) {
//                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
//            } else {
//                val intent = Intent(this, HomeActivity::class.java)
//                Toast.makeText(this, "登录成功，欢迎您，" + username, Toast.LENGTH_SHORT).show()
//                startActivity(intent)
//            }
           val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString("lastUsername", username)
            editor.apply()
//            if (prefs.contains("lastUsername")) {
//                Log.d("mainputlast", "yes")
//            } else {
//                Log.d("mainputlast", "no")
//            }
            val p: String = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            if (p != password) {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                cursor.close()
                userInfo.close()
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("username", username)
                Toast.makeText(this, "登录成功，欢迎您，" + username, Toast.LENGTH_SHORT).show()
                if (checkSave.isChecked) {
                    editor.putString("lastPassword", password)
                    editor.apply()
                } else {
                    editor.putString("lastPassword", "")
                    editor.apply()
                }
                startActivity(intent)
                Log.d("mainLogin", "yes")
                cursor.close()
                userInfo.close()
            }
        } else {
            Toast.makeText(this, "用户不存在，请先注册", Toast.LENGTH_SHORT).show()
            cursor.close()
            userInfo.close()
        }
    }

    fun handleRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}