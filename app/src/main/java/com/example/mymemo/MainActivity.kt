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
    }

    fun handleLogin(view: View) {
        val usernameText: EditText = findViewById(R.id.usernameText)
        val passwordText: EditText = findViewById(R.id.passwordText)
        val username: String = usernameText.text.toString()
        val password: String = passwordText.text.toString()
        val prefs: SharedPreferences = getSharedPreferences("user_information", MODE_PRIVATE)
        val isExist: Boolean = prefs.contains(username)
        if (username == "" || password == "") {
            Toast.makeText(this, "请填写完整", Toast.LENGTH_SHORT).show()
            return
        }
        if (isExist) {
            val p: String? = prefs.getString(username, "")
            if (p != password) {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                Toast.makeText(this, "登录成功，欢迎您，" + username, Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "用户不存在，请先注册", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}